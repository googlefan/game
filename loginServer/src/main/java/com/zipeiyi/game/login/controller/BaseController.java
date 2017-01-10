package com.zipeiyi.game.login.controller;

import com.zipeiyi.core.common.utils.JsonUtils;
import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangxiaoqiang on 16/10/25.
 */
public class BaseController extends WebController{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Map<String, Object> getResultMap() {

        return new LinkedHashMap<String, Object>();
    }

    protected String getIp(HttpServletRequest request){
        return IpUtil.getIP(request);
    }

    protected String toJson(int code, String msg) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("code", code);
        map.put("msg", msg);
        return JsonUtils.getJsonFromMap(map);
    }

    protected String toJson(int code, String msg, Object data) {
        Map<String, Object> result = getResultMap();
        result.put("code", code);
        result.put("msg", msg);
        if (data != null) {
            result.put("data", data);
        }

        return JsonUtils.getJsonFromMap(result);
    }
    
    protected String toJson(int code, String msg,int count,Object data) {
        Map<String, Object> result = getResultMap();
        result.put("code", code);
        result.put("msg", msg);
        result.put("total", count);
        if (data != null) {
            result.put("data", data);
        }

        return JsonUtils.getJsonFromMap(result);
    }

    protected String toJson2(int code, String msg) {
        Map<String, Object> result = getResultMap();
        result.put("code", code);
        result.put("msg", msg);

        return JsonUtils.toJson2(result);
    }

    protected String toJson2(int code, String msg, Object data) {
        Map<String, Object> result = getResultMap();
        result.put("code", code);
        result.put("msg", msg);
        if (data != null) {
            result.put("data", data);
        }

        return JsonUtils.toJson2(result);
    }

    protected String getResultJson(int code, String msg) {

        return toJson(code, msg, null);
    }

    protected String getSuccessResultJson() {
        return toJson(WebConstants.SUCCESS, "success", null);
    }

    protected String getSuccessResultJson(Map<String, ?> data) {
        return toJson(WebConstants.SUCCESS, "success", data);
    }


//    protected void opLog(String type, String desc) {
//        opLog(userHolder.getUser().getName(), type, desc, userHolder.getCurrentIpAddress());
//    }
//    /** opLog2("delete_user_sign", "删除用户标识：%s => %s", uid, sign); */
//    protected void opLog2(String type, String desc, Object... args) {
//        opLog(userHolder.getUser().getName(), type, desc, userHolder.getCurrentIpAddress(), args);
//    }
//
//    protected void opLog(String user, String type, String desc, String ip) {
//        opLog(user, type, desc, ip, new Object[0]);
//    }
//
//    protected void opLog(String user, String type, String desc, String ip, Object... args) {
//        String message = args == null || args.length == 0 ? desc : String.format(desc, args);
//        type = StringUtils.substring(type, 0, 20);
//        message = StringUtils.substring(message, 0, 500);
//        operationLogDao.addLog(user, type, message, ip);
//    }

    protected String send404() {
        return "forward:/error/404/";
    }

    protected String sendErrorMsg(Model model, String msg) {

        model.addAttribute("msg", msg);
        return "error/index";
    }

    protected String getRequestBody(HttpServletRequest request) throws Exception{
        BufferedReader br = null;
        String str, wholeStr = "";
        try{
            br = request.getReader();
            while((str = br.readLine()) != null) {
                wholeStr += str;
            }
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wholeStr;
    }
}
