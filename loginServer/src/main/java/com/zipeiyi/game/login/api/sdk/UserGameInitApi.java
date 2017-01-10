package com.zipeiyi.game.login.api.sdk;

import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.api.model.LoginRequest;
import com.zipeiyi.game.login.api.model.LoginResponse;
import com.zipeiyi.game.login.api.model.UserDataInitResponse;
import com.zipeiyi.game.login.api.model.UserDateInitRequest;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.xpower.http.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/12/26.
 */
@Service
public class UserGameInitApi {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginApi.class);
    private static final int HTTP_RETRY_NUM = 3;

    public UserDataInitResponse userDataInit(UserDateInitRequest userDateInitRequest) throws ApiRequestErrorException,HttpRequestException {
        long startTime = System.currentTimeMillis();
        try{
            Request req = new Request(WebConstants.CONFIG.get("userGameInitUrl"),"POST").retry(HTTP_RETRY_NUM).setEntity(userDateInitRequest.toString());
            req.setConnectionTimeout(60*1000).setSoTimeout(300*1000);
            req.addHeaders("Accept", "*/*");
            req.addHeaders("Content-Type", "application/json");
            String responseContent = req.execute().asString();
            UserDataInitResponse userDataInitResponse = UserDataInitResponse.parse(responseContent);
            if(userDataInitResponse != null && userDataInitResponse.code == 0){
                return userDataInitResponse;
            }else{
                if(userDataInitResponse != null){
                    throw new ApiRequestErrorException(userDataInitResponse.msg);
                }else{
                    throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
                }
            }
        }catch(Exception e){
            logger.error("userDataInit http request error,requestParam:{},",userDateInitRequest.toString(),e);
            throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("userDataInit http requestend cost:{},requestParam:{}",endTime-startTime,userDateInitRequest.toString());
        }
    }
}
