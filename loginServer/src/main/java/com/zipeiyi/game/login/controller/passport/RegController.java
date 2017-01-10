package com.zipeiyi.game.login.controller.passport;

import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.model.UserGateLocation;
import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.api.model.PhoneCodeResponse;
import com.zipeiyi.game.login.config.GateServerConfigLoad;
import com.zipeiyi.game.login.controller.ApiBaseController;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.game.login.exception.NoSuchUserException;
import com.zipeiyi.game.login.exception.UserBlockedException;
import com.zipeiyi.game.login.model.User;
import com.zipeiyi.game.login.model.UserConnect;
import com.zipeiyi.game.login.service.antispam.Frequency;
import com.zipeiyi.game.login.service.user.UserService;
import com.zipeiyi.game.login.utils.LoginTools;
import com.zipeiyi.game.login.utils.LoginUtil;
import com.zipeiyi.game.login.utils.UserAgentUtil;
import com.zipeiyi.game.login.utils.WebchatConnectUtil;
import com.zipeiyi.xpower.configcenter.ConfigCenter;
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
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Controller
@RequestMapping(value="passport")
public class RegController extends ApiBaseController {

    @Autowired
    private UserService userService;

    @Autowired
    Frequency frequency;

    private static final boolean NEED_PASSWORD = true;

    /**
     * 注册，验证手机号格式，密码格式，验证码有效性
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="register",method = {RequestMethod.POST})
    @ResponseBody
    public String reg(HttpServletRequest request,HttpServletResponse response) throws Exception{

        String phone = get(request,"account",null);
        String identifyCode = get(request,"identifyCode",null);
        String password = get(request,"password",null);
        String repassword = get(request,"repassword",null);
        int code = LoginTools.isInfoLegitimacy(password, repassword, phone);
        String errorMsg = LoginTools.getChineseErrorMsg(code);
        if(StringUtils.isNotEmpty(errorMsg)){
            return toJson(WebConstants.REG_ERROR_CODE,errorMsg);
        }

        //验证码＋手机号＋密码注册
        try{
            userService.checkRegist(phone, password, identifyCode);
            //同步数据注册成功后，本地库添加注册信息
            User user = new User();
            user.setSiteConnected(UserConnect.SITE_NOMAL);
            user.setAccount(phone);
            user.setName(phone);
            user.setRegTime(new Date());
            // 用原文传入，在service层做加密转换
            user.setRegIp(getIp(request));
            user.setAuth(User.AUTH_COMMON);
            user.setState(User.ACTIVE);
            long userId = userService.createUser(user, getIp(request),
                    UserService.NORMAL_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
            user.setId(userId);
            //注册后直接登录操作
           userService.registAndLogin(user,getIp(request),
                   UserService.NORMAL_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));

            //设置登录session
            LoginUtil.setLogin(request,response, user);

            //获取gateServer列表，根据负载均衡以及活跃状态分配gateServer地址
            UserGateLocation userGateLocation = (UserGateLocation) RedisUtil.getRedis().get(Constants.USER_GATE_SERVER_LOCATION_KEY + user.getId());
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
        }catch(ApiRequestErrorException e){
            return toJson(WebConstants.REG_ERROR_CODE,e.getMessage());
        }catch(HttpRequestException e){
            return toJson(WebConstants.REG_ERROR_CODE,e.getMessage());
        }

    }

    @RequestMapping(value="checkPhone",method={RequestMethod.POST})
    @ResponseBody
    public String checkPhone(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String phone = get(request,"account",null);
        if(StringUtils.isEmpty(phone)){
            return toJson(WebConstants.INPUT_PARAM_EMPTY_ERROR,"请输入手机号");
        }
        try{
            boolean isExist = userService.checkPhoneExist(phone);
            if(isExist){
                return toJson(WebConstants.ACCOUNT_REGISTED_ERROR,WebConstants.ACCOUNT_IS_REGISTER);
            }
        }catch(ApiRequestErrorException e){
            return toJson(WebConstants.ACCOUNT_REGISTED_ERROR,e.getMessage());
        }catch(HttpRequestException e){
            return toJson(WebConstants.REG_ERROR_CODE,e.getMessage());
        }
        return toJson(WebConstants.SUCCESS,"success");
    }

    @RequestMapping(value="sendSms",method={RequestMethod.POST})
    @ResponseBody
    public String sendSms(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String phone = get(request,"account",null);
        if(StringUtils.isEmpty(phone)){
            return toJson(WebConstants.INPUT_PARAM_EMPTY_ERROR,"请输入手机号");
        }
        /* 不管成功失败，1分钟内仅能试1次 */
        if (frequency.isTimesOverFlow(Frequency.FREQUENCY_SEND_SMS + phone, Frequency.MAX_SMS_TIMES)) {
            return toJson(WebConstants.SEND_SMS_FREQUENCY_LIMIT,WebConstants.FETCH_SECURITY_CODE_LIMIT_ERROR_MESSAGE);
        }
        frequency.addTimes(Frequency.FREQUENCY_SEND_SMS + phone, Frequency.FREQUENCY_SMS_TIME_LIMIT);

        try{
            PhoneCodeResponse phoneCodeResponse = userService.sendSecurityCode(phone);
            Map<String,Object> result = new HashMap<>();
            if(!ConfigCenter.isProduct()){
                result.put("isAlert",true);
                result.put("identifyCode",(String)phoneCodeResponse.data);
                return toJson(WebConstants.SUCCESS,"success",result);
            }else{
                result.put("isAlert",false);
                result.put("identifyCode","");
                return toJson(WebConstants.SUCCESS,"success",result);
            }
        }catch(ApiRequestErrorException e){
            return toJson(WebConstants.ACCOUNT_REGISTED_ERROR,e.getMessage());
        }catch(HttpRequestException e){
            return toJson(WebConstants.REG_ERROR_CODE,e.getMessage());
        }
    }
}
