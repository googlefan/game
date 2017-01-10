package com.zipeiyi.game.login.controller;

import com.zipeiyi.game.login.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangxiaoqiang on 16/12/15.
 */
@Controller
public class IndexController extends ApiBaseController{

    @RequestMapping(value="/")
    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception{
        return "redirect:" + WebConstants.LOGIN_MAIN;
    }
}
