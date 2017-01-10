package com.zipeiyi.game.login.controller.connect;

import com.zipeiyi.game.login.controller.BaseController;
import com.zipeiyi.game.login.utils.QQConnectUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="connect")
public class ConnectQQController extends BaseController {

    @RequestMapping(value="qq")
    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String origURL = get(request,"origURL","");
        return "redirect:" + QQConnectUtil.makeRequestURL(origURL);
    }
}
