package com.zipeiyi.game.login.controller.connect;

import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.controller.ApiBaseController;
import com.zipeiyi.game.login.model.User;
import com.zipeiyi.game.login.model.UserConnect;
import com.zipeiyi.game.login.service.user.UserConnectService;
import com.zipeiyi.game.login.service.user.UserService;
import com.zipeiyi.game.login.utils.LoginUtil;
import com.zipeiyi.game.login.utils.UserAgentUtil;
import com.zipeiyi.game.login.utils.WebchatConnectUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by zhangxiaoqiang on 16/12/15.
 */
@Controller
@RequestMapping(value="connect")
public class WebChatLoginController extends ApiBaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectService userConnectService;

    @RequestMapping(value="webchatLogin")
    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception{

        String code = get(request,"code",null);

        if (StringUtils.isBlank(code)) {
            return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
        }

        WebchatConnectUtil.WebchatToken webchatToken = WebchatConnectUtil.getAccessTokenAndOpenId(code);

        if (webchatToken == null || StringUtils.isEmpty(webchatToken.openId) || StringUtils.isEmpty(webchatToken.token)) {
            return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
        }

        UserConnect uc = userConnectService.getBySiteAndSiteEmail(UserConnect.SITE_QQ, WebchatConnectUtil.W_NAME
                + webchatToken.openId);
        if (uc == null) {
            User user = new User();
            user.setSiteConnected(UserConnect.SITE_WEIXIN);
            WebchatConnectUtil.WebchatUser webchatUser = WebchatConnectUtil.getUserName(webchatToken.token, webchatToken.openId);
            user.setAccount(WebchatConnectUtil.W_NAME + webchatToken.openId);
            user.setName(webchatUser == null ? "(weixin)" + WebConstants.DEFAULT_USER_NAME : (StringUtils.isNotEmpty(webchatUser.nickName) ? webchatUser.nickName : "(weixin)" + WebConstants.DEFAULT_USER_NAME));
            user.setRegTime(new Date());
            // 用原文传入，在service层做加密转换
            user.setRegIp(getIp(request));
            user.setAuth(User.AUTH_COMMON);
            user.setState(User.ACTIVE);
            long userId = userService.createUser(user, getIp(request),
                    UserService.WEIXIN_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));

            user.setId(userId);
            userConnectService.addUserConnect(userId, UserConnect.SITE_WEIXIN, userId, WebchatConnectUtil.W_NAME + webchatToken.openId, user.getName());


            // set account
            try {
                user = userService.login(user.getAccount(), null, false,getIp(request),UserService.WEIXIN_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
                LoginUtil.setLogin(request,response, user);
            }catch (Exception e) {
                logger.error("weixin第三方登陆，调用登陆接口异常，",e);
                return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
            }
        }else {
            User user = userService.getUser(uc.getUserId());
            if (user == null) {
                return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
            }

            try {
                user = userService.login(user.getAccount(), null, false,getIp(request),UserService.WEIXIN_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
                LoginUtil.setLogin(request,response, user);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:" + WebConstants.LOGIN_SUCCESS;
    }
}
