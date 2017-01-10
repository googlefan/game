package com.zipeiyi.game.login.controller.connect;

import com.zipeiyi.core.common.utils.XSSUtil;
import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.controller.BaseController;
import com.zipeiyi.game.login.model.User;
import com.zipeiyi.game.login.model.UserConnect;
import com.zipeiyi.game.login.service.user.UserConnectService;
import com.zipeiyi.game.login.service.user.UserService;
import com.zipeiyi.game.login.utils.LoginUtil;
import com.zipeiyi.game.login.utils.QQConnectUtil;
import com.zipeiyi.game.login.utils.UserAgentUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping(value="connect")
public class QQLoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectService userConnectService;

    @RequestMapping(value="qqLogin")
    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception{

        String code = get(request,"code",null);
        String origURL = get(request,"origURL",null);

        if (StringUtils.isBlank(code)) {
            return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
        }

        String accessToken = QQConnectUtil.getAccessToken(code, origURL);

        if (StringUtils.isBlank(accessToken)) {
            return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
        }

        String openId = QQConnectUtil.getOpenId(accessToken);

        if (StringUtils.isBlank(openId)) {
            return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
        }

        openId = StringUtils.lowerCase(openId);

        UserConnect uc = userConnectService.getBySiteAndSiteEmail(UserConnect.SITE_QQ, QQConnectUtil.Q_NAME
                + openId);
        if (uc == null) {
            User user = new User();
            user.setSiteConnected(UserConnect.SITE_QQ);
            String userName = XSSUtil.getHtmlRemoving(QQConnectUtil.getUserName(accessToken, openId));
            user.setAccount(QQConnectUtil.Q_NAME + openId);
            user.setName(StringUtils.isBlank(userName) ? "(QQ)" + WebConstants.DEFAULT_USER_NAME : userName);
            user.setRegTime(new Date());
            // 用原文传入，在service层做加密转换
            user.setRegIp(getIp(request));
            user.setAuth(User.AUTH_COMMON);
            user.setState(User.ACTIVE);
            long userId = userService.createUser(user, getIp(request),
                    UserService.QQ_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));

            user.setId(userId);

            if(StringUtils.isBlank(userName)){
                userConnectService.addUserConnect(userId, UserConnect.SITE_QQ, userId, QQConnectUtil.Q_NAME + openId, "(QQ)" + WebConstants.DEFAULT_USER_NAME);
            }
            else{
                userConnectService.addUserConnect(userId, UserConnect.SITE_QQ, userId, QQConnectUtil.Q_NAME + openId, userName);
            }

            // set account
            try {
                user = userService.login(user.getAccount(), null, false,getIp(request),UserService.QQ_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
                LoginUtil.setLogin(request,response, user);
            }catch (Exception e) {
                logger.error("qq第三方登陆，调用登陆接口异常，",e);
                return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
            }
        }else {
            User user = userService.getUser(uc.getUserId());
            if (user == null) {
                return "redirect:" + WebConstants.LOGIN_MAIN + "?error=1";
            }

            try {
                user = userService.login(user.getAccount(), null, false,getIp(request),UserService.QQ_LOGIN, UserAgentUtil.guessUserAgent(request.getHeader("User-Agent")));
                LoginUtil.setLogin(request,response, user);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:" + WebConstants.LOGIN_SUCCESS;
    }
}
