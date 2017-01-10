package com.zipeiyi.game.login.controller;

import com.zipeiyi.game.login.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zhangxiaoqiang on 16/12/15.
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/403")
    public String deny(Model model) {
        return "redirect:" + WebConstants.ERROR_INDEX_PAGE + "?code=403";
    }

    @RequestMapping("/404")
    public String pageNotFound(Model model) {
        return "redirect:" + WebConstants.ERROR_INDEX_PAGE + "?code=404";
    }

    @RequestMapping("/500")
    public String intenetError(Model model,Exception ex) {
        return "redirect:" + WebConstants.ERROR_INDEX_PAGE + "?code=500";
    }

    public String error(Model model,int code,String msg) {
        model.addAttribute("code",code);
        model.addAttribute("msg",msg);
        return "redirect:" + WebConstants.ERROR_INDEX_PAGE + "?code=" + code + "&msg=" + msg;
    }

    @RequestMapping("*")
    public String unknowError(Model model) {
        return error(model,999,"unknow error");
    }
}
