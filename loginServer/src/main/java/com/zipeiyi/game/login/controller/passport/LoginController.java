package com.zipeiyi.game.login.controller.passport;

import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.model.UserGateLocation;
import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.config.GateServerConfigLoad;
import com.zipeiyi.game.login.controller.ApiBaseController;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.game.login.exception.NoSuchUserException;
import com.zipeiyi.game.login.exception.UserBlockedException;
import com.zipeiyi.game.login.model.User;
import com.zipeiyi.game.login.service.antispam.Frequency;
import com.zipeiyi.game.login.service.user.UserService;
import com.zipeiyi.game.login.utils.LoginUtil;
import com.zipeiyi.game.login.utils.UserAgentUtil;
import com.zipeiyi.game.login.utils.ValidateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 登录公共controller
 * author:zhangxiaoqiang
 */
@Controller
@RequestMapping(value="passport")
public class LoginController extends ApiBaseController {

    @Autowired
    UserService userService;

    @Autowired
    Frequency frequency;

    private static final boolean NEED_PASSWORD = true;

    private static final boolean NO_NEED_PASSWORD = false;

    @RequestMapping(value="preLogin")
    @ResponseBody
    public String preLogin(HttpServletRequest request,HttpServletResponse reponse)throws Exception {

        User user = LoginUtil.getLoginSession(request);
        if(user != null){
            //已经登陆，刷新或者新开浏览器操作，需要更新token剔除原登陆
            Map<String,Object> map = new HashMap<>();
            map.put("userName",user.getName());
            map.put("loginType",user.getSiteConnected());
            return toJson(WebConstants.SUCCESS,"success",map);
        }else{
            return toJson(WebConstants.NOT_LOGIN,WebConstants.NOT_LOGIN_MESSAGE);
        }
    }

    @RequestMapping(value="quickLogin",method = {RequestMethod.POST})
    @ResponseBody
    public String quickLogin(HttpServletRequest request,HttpServletResponse response)throws Exception{
        int loginType = get(request,"loginType",0);
        User user = LoginUtil.getLoginSession(request);
        if(user == null){
            return toJson(WebConstants.NOT_LOGIN,WebConstants.NOT_LOGIN_MESSAGE);
        }else{
            //登陆流程
            /* 登录 */
            try {
                user = userService.login(user.getAccount(), null, NO_NEED_PASSWORD, getIp(request), loginType, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
            } catch (NoSuchUserException e) {
                return toJson(WebConstants.USER_NOT_EXIST_ERROR,WebConstants.USER_NOT_EXIST_ERROR_MESSAGE);
            }catch (UserBlockedException e) {
                return toJson(WebConstants.USER_BLOCK_ERROR,WebConstants.USER_BLOCK_ERROR_MSG);
            }catch(ApiRequestErrorException e){
                return toJson(WebConstants.API_REQUEST_ERROR,e.getMessage());
            }catch(HttpRequestException e){
                return toJson(WebConstants.API_REQUEST_ERROR,e.getMessage());
            }

            //设置登录session
            LoginUtil.setLogin(request,response, user);

            //获取gateServer列表，根据负载均衡以及活跃状态分配gateServer地址
            UserGateLocation userGateLocation = (UserGateLocation)RedisUtil.getRedis().get(Constants.USER_GATE_SERVER_LOCATION_KEY + user.getId());
            List<GateServerConfigLoad.GateServer> gateServers =  GateServerConfigLoad.get().gateServerList;
            GateServerConfigLoad.GateServer gs = null;
            Map<String,Object> result = new HashMap<>();
            if(userGateLocation != null){
                //重连活跃通道gateServer
                for(GateServerConfigLoad.GateServer gateServer : gateServers){
                    if(gateServer.hostName.equals(userGateLocation.getHostName())){
                        gs = gateServer;
                        break;
                    }
                }
                if(gs != null){
                    result.put("ip",gs.ip);
                    result.put("port",gs.port);
                    result.put("ticket",user.getToken());
                    result.put("uid",user.getId());
                    return toJson(WebConstants.SUCCESS,"success",result);
                }else{
                    //没有找到主机地址，可能已经下线或者配置错误
                    return toJson(WebConstants.GATE_SERVER_CONNECT_ERROR,WebConstants.GATE_SERVER_CONNECT_ERROR_MSG);
                }
            }else{
                //负载选择通道gateServer
                Collections.sort(gateServers, new Comparator<GateServerConfigLoad.GateServer>() {
                    @Override
                    public int compare(GateServerConfigLoad.GateServer o1, GateServerConfigLoad.GateServer o2) {
                        if (o1.connect - o2.connect >= 0) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                gs = gateServers.get(0);
                result.put("ip",gs.ip);
                result.put("port",gs.port);
                result.put("ticket",user.getToken());
                result.put("uid",user.getId());
                return toJson(WebConstants.SUCCESS,"success",result);
            }
        }
    }

    @RequestMapping(value="login",method = {RequestMethod.POST})
    @ResponseBody
    public String login(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String account = get(request,"account",null);
        String password = get(request,"password",null);
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return toJson(WebConstants.ACCOUNT_INPUT_ERROR,WebConstants.ACCOUNT_INPUT_ERROR_MESSAGE);
        }

        //用户名是否是真实手机号验证
        if(!ValidateUtil.validateMoblie(account)){
            return toJson(WebConstants.ACCOUNT_FOMAT_ERROR,WebConstants.ACCOUNT_FOMAT_ERROR_MESSAGE);
        }

        /* 开始登陆操作 */
        User user = null;

        /* 不管成功失败，1分钟内仅能试5次 */
        if (frequency.isTimesOverFlow(Frequency.FREQUENCY_LOGIN + account, Frequency.MAX_LOGIN_TIMES)) {
            return toJson(WebConstants.LOGIN_FREQUENCY_LIMIT,WebConstants.LOGIN_FREQUENCY_LIMIT_ERORR_MESSAGE);
        }
        frequency.addTimes(Frequency.FREQUENCY_LOGIN + account, Frequency.FREQUENCY_TIME_LIMIT);


        /* 登录 */
        try {
            user = userService.login(account, password, NEED_PASSWORD, getIp(request), UserService.NORMAL_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
        } catch (NoSuchUserException e) {
            return toJson(WebConstants.USER_NOT_EXIST_ERROR,WebConstants.USER_NOT_EXIST_ERROR_MESSAGE);
        }catch (UserBlockedException e) {
            return toJson(WebConstants.USER_BLOCK_ERROR,WebConstants.USER_BLOCK_ERROR_MSG);
        }catch(ApiRequestErrorException e){
            return toJson(WebConstants.API_REQUEST_ERROR,e.getMessage());
        }catch(HttpRequestException e){
            return toJson(WebConstants.API_REQUEST_ERROR,e.getMessage());
        }

        //设置登录session
        LoginUtil.setLogin(request, response,user);

        //获取gateServer列表，根据负载均衡以及活跃状态分配gateServer地址
        UserGateLocation userGateLocation = (UserGateLocation)RedisUtil.getRedis().get(Constants.USER_GATE_SERVER_LOCATION_KEY + user.getId());
        List<GateServerConfigLoad.GateServer> gateServers =  GateServerConfigLoad.get().gateServerList;
        GateServerConfigLoad.GateServer gs = null;
        Map<String,Object> result = new HashMap<>();
        if(userGateLocation != null){
            //重连活跃通道gateServer
            for(GateServerConfigLoad.GateServer gateServer : gateServers){
                if(gateServer.hostName.equals(userGateLocation.getHostName())){
                    gs = gateServer;
                    break;
                }
            }
            if(gs != null){
                result.put("ip",gs.ip);
                result.put("port",gs.port);
                result.put("ticket",user.getToken());
                result.put("uid",user.getId());
                return toJson(WebConstants.SUCCESS,"success",result);
            }else{
                //没有找到主机地址，可能已经下线或者配置错误
                return toJson(WebConstants.GATE_SERVER_CONNECT_ERROR,WebConstants.GATE_SERVER_CONNECT_ERROR_MSG);
            }
        }else{
            //负载选择通道gateServer
            Collections.sort(gateServers, new Comparator<GateServerConfigLoad.GateServer>() {
                @Override
                public int compare(GateServerConfigLoad.GateServer o1, GateServerConfigLoad.GateServer o2) {
                    if (o1.connect - o2.connect >= 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            gs = gateServers.get(0);
            result.put("ip",gs.ip);
            result.put("port",gs.port);
            result.put("ticket",user.getToken());
            result.put("uid",user.getId());
            return toJson(WebConstants.SUCCESS,"success",result);
        }
    }
}
