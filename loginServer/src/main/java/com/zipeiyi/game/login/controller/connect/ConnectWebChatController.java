package com.zipeiyi.game.login.controller.connect;

import com.zipeiyi.game.login.controller.ApiBaseController;
import com.zipeiyi.game.login.utils.WebchatConnectUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangxiaoqiang on 16/12/15.
 */
@Controller
@RequestMapping(value="connect")
public class ConnectWebChatController extends ApiBaseController{

    @RequestMapping(value="webchat")
    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String origURL = get(request,"origURL","");
        return "redirect:" + WebchatConnectUtil.makeRequestURL(origURL);
    }
}
