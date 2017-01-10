package com.zipeiyi.game.login.service.user;


import com.zipeiyi.game.login.api.model.PhoneCodeResponse;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.game.login.exception.NoSuchUserException;
import com.zipeiyi.game.login.exception.UserBlockedException;
import com.zipeiyi.game.login.model.User;

/**
 * 有关用户的类
 *
 * @author zhangxiaoqiang
 *
 */
public interface UserService {

    public static final int NORMAL_LOGIN = 10;

    public static final int QQ_LOGIN = 20;

    public static final int WEIXIN_LOGIN = 30;

    public User getUser(long id);

    public long createUser(User user, String regIp, int regType, String userAgent) throws Exception;

    public void checkRegist(String phone, String password, String identifyCode) throws ApiRequestErrorException, HttpRequestException;

    public boolean checkPhoneExist(String phone) throws ApiRequestErrorException, HttpRequestException;

    public PhoneCodeResponse sendSecurityCode(String phone) throws ApiRequestErrorException, HttpRequestException;

    /**
     * 注册同时登录，不需要验证的登录密码，直接登录，生成登录相关信息
     * @param userIp
     * @param userLoginType
     * @param userAgent
     * @return
     */
    public User registAndLogin(User user,String userIp,int userLoginType,String userAgent) throws ApiRequestErrorException, HttpRequestException ;

    /**
     * 用户登录（威力加强版）
     */
    public User login(String account, String password, boolean needPassword, String userIp, int userLoginType, String userAgent)
            throws NoSuchUserException, UserBlockedException, ApiRequestErrorException, HttpRequestException;

    /**
     * 更改用户状态，激活，未激活，封禁等
     */
    public void modifyUserState(long userId, int state);

    public void addLoginNum(long userId);

    public User getUserByAccount(String account);

    public void modifyUserPassword(String phone, String password, String securityCode) throws ApiRequestErrorException, HttpRequestException;
}
