package com.zipeiyi.game.login.controller.passport;

import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.controller.ApiBaseController;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.game.login.model.User;
import com.zipeiyi.game.login.service.user.UserService;
import com.zipeiyi.game.login.utils.LoginTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Controller
@RequestMapping(value="passport")
public class PasswordRetrieveController extends ApiBaseController{

   @Autowired
   private UserService userService;

   @RequestMapping(value = "retrievePassCheck",method={RequestMethod.POST})
   @ResponseBody
   public String retrieveAccountCheck(HttpServletRequest request,HttpServletResponse response) throws Exception{

       String phone = get(request,"account",null);
       if(StringUtils.isEmpty(phone)){
           return toJson(WebConstants.INPUT_PARAM_EMPTY_ERROR,"请输入手机号");
       }
       User user = userService.getUserByAccount(phone);
       if(user == null){
           return toJson(WebConstants.USER_NOT_EXIST_ERROR,WebConstants.USER_NOT_EXIST_ERROR_MESSAGE);
       }
       return toJson(WebConstants.SUCCESS,"success");
   }

   @RequestMapping(value="retrievePass",method = {RequestMethod.POST})
   @ResponseBody
   public String retrievePass(HttpServletRequest request,HttpServletResponse response) throws Exception{
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
           userService.modifyUserPassword(phone,password,identifyCode);
       }catch(ApiRequestErrorException e){
           return toJson(WebConstants.REG_ERROR_CODE,e.getMessage());
       }catch(HttpRequestException e){
           return toJson(WebConstants.REG_ERROR_CODE,e.getMessage());
       }

       return toJson(WebConstants.SUCCESS,"success");
   }
}
