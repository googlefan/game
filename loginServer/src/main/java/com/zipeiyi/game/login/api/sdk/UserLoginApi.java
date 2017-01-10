package com.zipeiyi.game.login.api.sdk;

import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.api.model.*;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.xpower.http.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Service
public class UserLoginApi {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginApi.class);
    private static final int HTTP_RETRY_NUM = 3;

    public LoginResponse loginCheck(LoginRequest loginRequest) throws ApiRequestErrorException,HttpRequestException{
        long startTime = System.currentTimeMillis();
        try{
            Request req = new Request(WebConstants.CONFIG.get("loginCheckUrl"),"POST").retry(HTTP_RETRY_NUM).setEntity(loginRequest.toString());
            req.setConnectionTimeout(60*1000).setSoTimeout(300*1000);
            req.addHeaders("Accept", "*/*");
            req.addHeaders("Content-Type", "application/json");
            String responseContent = req.execute().asString();
            LoginResponse loginResponse = LoginResponse.parse(responseContent);
            if(loginResponse != null && loginResponse.status.equals("0")){
                return loginResponse;
            }else{
                if(loginResponse != null){
                    throw new ApiRequestErrorException(loginResponse.errorInfo);
                }else{
                    throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
                }
            }
        }catch(Exception e){
            logger.error("logincheck http request error,requestParam:{},",loginRequest.toString(),e);
            if(e instanceof ApiRequestErrorException){
                throw (ApiRequestErrorException) e;
            }else{
                throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
            }
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("logincheck http requestend cost:{},requestParam:{}",endTime-startTime,loginRequest.toString());
        }
    }

    /**
     * true 代表用户存在，不能重复注册
     * @param account
     * @return
     * @throws ApiRequestErrorException
     * @throws HttpRequestException
     */
    public boolean checkPhoneExist(String account) throws ApiRequestErrorException,HttpRequestException{
        long startTime = System.currentTimeMillis();
        try{
            Request req = new Request(getPhoneExistCheckUrl(account)).retry(HTTP_RETRY_NUM);
            req.setConnectionTimeout(60*1000).setSoTimeout(60*1000);
            req.addHeaders("Content-Type", "text/xml; charset=utf-8");
            String responseContent = req.execute().asString();
            PhoneCheckResponse phoneCheckResponse = PhoneCheckResponse.parse(responseContent);
            if(phoneCheckResponse != null && phoneCheckResponse.status.equals("1") && phoneCheckResponse.code.equals("REGIST0001")){
               return true;
            }else if(phoneCheckResponse != null && phoneCheckResponse.status.equals("0")){
                return false;
            }else{
                throw new ApiRequestErrorException(WebConstants.USER_NOT_EXIST_ERROR_MESSAGE);
            }
        }catch(Exception e){
            logger.error("checkPhoneExist http request error,requestParam:{},",account,e);
            if(e instanceof ApiRequestErrorException){
                throw (ApiRequestErrorException)e;
            }
            throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("checkPhoneExist http requestend cost:{},requestParam:{}",endTime-startTime,account);
        }
    }

    public RegisterResponse checkRegister(RegisterRequest registerRequest) throws ApiRequestErrorException,HttpRequestException{
        long startTime = System.currentTimeMillis();
        try{
            Request req = new Request(WebConstants.CONFIG.get("regCheckUrl"),"POST").retry(HTTP_RETRY_NUM).setEntity(registerRequest.toString());
            req.setConnectionTimeout(60*1000).setSoTimeout(300*1000);
            req.addHeaders("Accept", "*/*");
            req.addHeaders("Content-Type", "application/json");
            String responseContent = req.execute().asString();
            RegisterResponse registerResponse = RegisterResponse.parse(responseContent);
            if(registerResponse != null && registerResponse.status.equals("0")){
                return registerResponse;
            }else{
                if(registerResponse != null){
                    throw new ApiRequestErrorException(registerResponse.errorInfo);
                }else{
                    throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
                }
            }
        }catch(Exception e){
            logger.error("checkRegister http request error,requestParam:{},",registerRequest.toString(),e);
            if(e instanceof ApiRequestErrorException){
                throw (ApiRequestErrorException)e;
            }
            throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("checkRegister http requestend cost:{},requestParam:{}",endTime-startTime,registerRequest.toString());
        }
    }

    public PhoneCodeResponse sendSms(PhoneCodeRequest phoneCodeRequest) throws ApiRequestErrorException,HttpRequestException{
        long startTime = System.currentTimeMillis();
        try{
            Request req = new Request(WebConstants.CONFIG.get("phoneCodeUrl"),"POST").retry(HTTP_RETRY_NUM).setEntity(phoneCodeRequest.toString());
            req.setConnectionTimeout(60*1000).setSoTimeout(300*1000);
            req.addHeaders("Accept", "*/*");
            req.addHeaders("Content-Type", "application/json");
            String responseContent = req.execute().asString();
            PhoneCodeResponse phoneCodeResponse = PhoneCodeResponse.parse(responseContent);
            if(phoneCodeResponse != null && phoneCodeResponse.status.equals("0")){
                return phoneCodeResponse;
            }else{
                if(phoneCodeResponse != null){
                    throw new ApiRequestErrorException(phoneCodeResponse.errorInfo);
                }else{
                    throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
                }
            }
        }catch(Exception e){
            logger.error("sendSms http request error,requestParam:{},",phoneCodeRequest.toString(),e);
            if(e instanceof ApiRequestErrorException){
                throw (ApiRequestErrorException)e;
            }
            throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("sendSms http requestend cost:{},requestParam:{}",endTime-startTime,phoneCodeRequest.toString());
        }
    }

    public ForgetPassResponse forgetPasswordCheck(ForgetPassRequest forgetPassRequest) throws ApiRequestErrorException,HttpRequestException{
        long startTime = System.currentTimeMillis();
        try{
            Request req = new Request(WebConstants.CONFIG.get("forgetPassUrl"),"POST").retry(HTTP_RETRY_NUM).setEntity(forgetPassRequest.toString());
            req.setConnectionTimeout(60*1000).setSoTimeout(300*1000);
            req.addHeaders("Accept", "*/*");
            req.addHeaders("Content-Type", "application/json");
            String responseContent = req.execute().asString();
            ForgetPassResponse forgetPassResponse = ForgetPassResponse.parse(responseContent);
            if(forgetPassResponse != null && forgetPassResponse.status.equals("0")){
                return forgetPassResponse;
            }else{
                if(forgetPassResponse != null){
                    throw new ApiRequestErrorException(forgetPassResponse.errorInfo);
                }else{
                    throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
                }
            }
        }catch(Exception e){
            logger.error("forgetPasswordCheck http request error,requestParam:{},",forgetPassRequest.toString(),e);
            if(e instanceof ApiRequestErrorException){
                throw (ApiRequestErrorException)e;
            }
            throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("forgetPasswordCheck http requestend cost:{},requestParam:{}",endTime-startTime,forgetPassRequest.toString());
        }
    }

    private String getPhoneExistCheckUrl(String account){
        return WebConstants.CONFIG.get("phoneCheckUrl") + "/" + account;
    }


}
