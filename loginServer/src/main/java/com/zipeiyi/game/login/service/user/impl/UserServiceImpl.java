package com.zipeiyi.game.login.service.user.impl;

import com.zipeiyi.core.service.IdCenterService;
import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.common.util.MD5Util;
import com.zipeiyi.game.common.util.RandomUtil;
import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.api.model.*;
import com.zipeiyi.game.login.api.sdk.UserGameInitApi;
import com.zipeiyi.game.login.api.sdk.UserLoginApi;
import com.zipeiyi.game.login.dao.LoginRecordDao;
import com.zipeiyi.game.login.dao.RegRecordDao;
import com.zipeiyi.game.login.dao.UserDao;
import com.zipeiyi.game.login.exception.ApiRequestErrorException;
import com.zipeiyi.game.login.exception.HttpRequestException;
import com.zipeiyi.game.login.exception.NoSuchUserException;
import com.zipeiyi.game.login.exception.UserBlockedException;
import com.zipeiyi.game.login.model.*;
import com.zipeiyi.game.login.service.user.UserInitService;
import com.zipeiyi.game.login.service.user.UserService;
import com.zipeiyi.game.login.service.user.UserTicketService;
import com.zipeiyi.game.login.utils.LoginTools;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value="userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserLoginApi userLoginApi;

    @Autowired
    UserGameInitApi userGameInitApi;

    @Autowired
    UserTicketService userTicketService;

    @Autowired
    UserInitService userInitService;

    @Autowired
    IdCenterService idCenterService;

    @Autowired
    LoginRecordDao loginRecordDao;

    @Autowired
    RegRecordDao regRecordDao;

    @Autowired
    UserDao userDao;

    private static final int TICKET_LENTH = 20;

    private static final int USER_KEEP_TIME = 60*60;

    private static final int GAME_USER_FIGURE_SIZE = 6;

    private static final Logger logger =  LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 用户登录
     */
    @Override
    public User login(String account, String password, boolean needPassword,String userIp,int userLoginType,String userAgent) throws NoSuchUserException, UserBlockedException, ApiRequestErrorException, HttpRequestException {

        User user = getUserByAccount(account);

        validatePassword(password, needPassword, user);

        //生成user ticket
        user = setTicket(user, userIp, userLoginType, userAgent);

        //每次登陆，登陆次数＋1
        addLoginNum(user.getId());
        return user;

    }

    private void validatePassword(String password, boolean needPassword, User user) throws NoSuchUserException, UserBlockedException, ApiRequestErrorException, HttpRequestException {
        if (user == null) {
            throw new NoSuchUserException();
        }
        if (user.isClosure()) {
            throw new UserBlockedException();
        }

        // password
        if (needPassword) {
            //TO DO:调用一部接口实现登陆密码验证，并将用户保存到游戏注册列表中
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.registId = user.getAccount();
            loginRequest.loginPwd = password;
            LoginResponse loginResponse = userLoginApi.loginCheck(loginRequest);
            //将登陆返回信息插入到当前user表中,并设置userId（由于资配易帐户输入密码后暂时封禁，所以暂时不检验资配易帐户的封禁）
            if(user.getId() <=  0){
                user.setAuth(User.AUTH_COMMON);
                user.setState(User.ACTIVE);
                user.setSiteConnected(UserConnect.SITE_NOMAL);
                try {
                    user.setId(idCenterService.getId(""));
                } catch (TException e) {
                    logger.error("生成用户id异常，",e);
                    throw new ApiRequestErrorException("系统异常，稍后重试");
                }
                user.setRegIp("");
                user.setRegTime(new Date(loginResponse.data.registTime/1000));
                //设置游戏用户头像，目前只有6个，随机
                int figureIndex = RandomUtil.generateRandomIndex(GAME_USER_FIGURE_SIZE);
                user.setFigure(figureIndex);
                userDao.insert(user);
                //原资配易账户用户，登录需要初始化游戏中的数据状态
                logger.info("create user game info in method login,uid:{}",user.getId());
//                UserDateInitRequest userDateInitRequest = new UserDateInitRequest();
//                userDateInitRequest.uid = user.getId();
//                userDateInitRequest.timestamp = new Date().getTime();
//                userDateInitRequest.sign = CmdSignUtil.getUserInfoSign(user.getId(),userDateInitRequest.timestamp);
//               userGameInitApi.userDataInit(userDateInitRequest);
                userInitService.initUser(user.getId());

                RedisUtil.getRedis().set(Constants.USER_BASIC_INFO + user.getId(),USER_KEEP_TIME,user);
                RedisUtil.getRedis().set(Constants.USER_ACCOUNT_BASIC_INFO + user.getAccount(),USER_KEEP_TIME,user);
            }
        }
    }

    /**
     * 生成T票，存储T票并生成登陆记录
     */
    private User setTicket(User user, String userIp, int userLoginType, String userAgent) {

        UserTicket userTicket = new UserTicket();

        // account ticket
        Date now = new Date();
        userTicket.setLoginTime(now);
        userTicket.setUserId(user.getId());
        userTicket.setTicket(LoginTools.getRandString(TICKET_LENTH));

        userTicketService.createOrUpdateTicket(userTicket);
        user.setToken(userTicket.getTicket());

        // 用户登录时间和登录ip更新
        user.setLastLoginIp(userIp);
        userDao.updateLoginTime(user);

        // 登录记录
        try {
            LoginRecord lr = new LoginRecord();
            lr.setIp(userIp);
            lr.setType(userLoginType);
            lr.setUserId(user.getId());
            lr.setUserAgent(userAgent);
            loginRecordDao.insertLoginRecord(lr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 创建用户，基本的
     */
    private long createUser(User user) throws TException {
        long userId = idCenterService.getId("");
        user.setId(userId);
        userDao.insert(user);
        //创建用户进行缓存
        User queryUser = getUser(userId);
        return queryUser.getId();
    }

    /**
     * 通用建立用户，包含日志(用户第三方登陆以及普通注册)
     */
    @Override
    public long createUser(User user,String regIp, int regType, String userAgent) throws Exception{
        //随机头像索引
        int figureIndex = RandomUtil.generateRandomIndex(GAME_USER_FIGURE_SIZE);
        user.setFigure(figureIndex);
        long userId = createUser(user);

        try {
            RegRecord regRecord = new RegRecord();
            regRecord.setIp(StringUtils.isBlank(regIp) ? "127.0.0.1" : regIp);
            regRecord.setType(regType);
            regRecord.setUserAgent(userAgent);
            regRecord.setUserId(userId);
            regRecordDao.insertRegRecord(regRecord);
        } catch (Exception e) {
            logger.error("regRecord error ", e);
        }

        return userId;
    }

    @Override
    public void checkRegist(String phone, String password, String identifyCode) throws ApiRequestErrorException, HttpRequestException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.phone = phone;
        registerRequest.loginPwd = password;
        registerRequest.securityCode = identifyCode;
        userLoginApi.checkRegister(registerRequest);
    }

    @Override
    public boolean checkPhoneExist(String phone) throws ApiRequestErrorException, HttpRequestException {
        try {
            Boolean isExist = userLoginApi.checkPhoneExist(phone);
            return isExist;
        } catch (ApiRequestErrorException e) {
            throw new ApiRequestErrorException(WebConstants.ACCOUNT_API_REQUEST_PARSE_ERROR);
        } catch (HttpRequestException e) {
            throw new HttpRequestException(WebConstants.HTTP_REQUEST_EXCEPTION_MSG);
        }
    }

    @Override
    public PhoneCodeResponse sendSecurityCode(String phone) throws ApiRequestErrorException, HttpRequestException {
        try {
            PhoneCodeRequest phoneCodeRequest = new PhoneCodeRequest();
            phoneCodeRequest.phorem = phone;
            PhoneCodeResponse phoneCodeResponse = userLoginApi.sendSms(phoneCodeRequest);
            return phoneCodeResponse;
        } catch (ApiRequestErrorException e) {
            throw new ApiRequestErrorException(e.getMessage());
        } catch (HttpRequestException e) {
            throw new HttpRequestException("服务器异常，稍后重试");
        }
    }

    @Override
    public User registAndLogin(User user, String userIp, int userLoginType, String userAgent) throws ApiRequestErrorException, HttpRequestException  {
        //生成user ticket
        user = setTicket(user, userIp, userLoginType, userAgent);

        //每次登陆，登陆次数＋1
        addLoginNum(user.getId());
        //初始化游戏的基本信息
        logger.info("create user game info in method registAndLogin,uid:{}",user.getId());
        //to do,使用md5（uid+随机串+时间戳）加密传输
//        UserDateInitRequest userDateInitRequest = new UserDateInitRequest();
//        userDateInitRequest.uid = user.getId();
//        userDateInitRequest.timestamp = new Date().getTime();
//        userDateInitRequest.sign = CmdSignUtil.getUserInfoSign(user.getId(),userDateInitRequest.timestamp);
//        userGameInitApi.userDataInit(userDateInitRequest);
        userInitService.initUser(user.getId());
        return user;
    }


    @Override
    public User getUser(long id) {
        Object obj = RedisUtil.getRedis().get(Constants.USER_BASIC_INFO + id);
        if (obj != null && obj instanceof User) {
            return (User) obj;
        }
        User user = userDao.getUser(id);
        RedisUtil.getRedis().set(Constants.USER_BASIC_INFO + id, USER_KEEP_TIME, user);
        RedisUtil.getRedis().set(Constants.USER_ACCOUNT_BASIC_INFO + user.getAccount(),USER_KEEP_TIME,user);
        return user;
    }


    @Override
    public void modifyUserState(long userId, int state) {
        userDao.modifyUserState(userId, state);
        cleanCache(userId);
    }

    /**
     * 目前每次登陆更改一次数据库，后续性能优化可以改造成隔一定时间批量插入
     * @param userId
     */
    @Override
    public void addLoginNum(long userId) {
        userDao.addLoginNum(userId);
    }

    @Override
    public User getUserByAccount(String account) {
       //用户信息可以增加缓存,先获取本地的，如果本地没有，调用一部接口
        User user = null;
        user = (User)RedisUtil.getRedis().get(Constants.USER_ACCOUNT_BASIC_INFO + account);
        if(user != null){
            return user;
        }
        user = userDao.fetchUserByAccount(account);
        if(user != null){
            RedisUtil.getRedis().set(Constants.USER_BASIC_INFO + user.getId(),USER_KEEP_TIME,user);
            RedisUtil.getRedis().set(Constants.USER_ACCOUNT_BASIC_INFO + user.getAccount(),USER_KEEP_TIME,user);
            return user;
        }
        try {
            Boolean isExist = userLoginApi.checkPhoneExist(account);
            if(isExist){
                User newUser = new User();
                newUser.setAccount(account);
                return newUser;
            }
        } catch (ApiRequestErrorException e) {
            logger.error("check phone exist error");
        } catch (HttpRequestException e) {
            logger.error("check phone exist error");
        }
        return null;
    }

    @Override
    public void modifyUserPassword(String phone, String password, String securityCode) throws ApiRequestErrorException, HttpRequestException {
        try {
            ForgetPassRequest forgetPassRequest = new ForgetPassRequest();
            forgetPassRequest.phorem = phone;
            forgetPassRequest.loginNewPwd = password;
            forgetPassRequest.securityCode = securityCode;
            userLoginApi.forgetPasswordCheck(forgetPassRequest);
        } catch (ApiRequestErrorException e) {
            throw new ApiRequestErrorException(e.getMessage());
        } catch (HttpRequestException e) {
            throw new HttpRequestException("服务器异常，稍后重试");
        }
    }

    private void cleanCache(long userId){
        User user = (User)RedisUtil.getRedis().get(Constants.USER_BASIC_INFO + userId);
        if(user != null){
            RedisUtil.getRedis().delete(Constants.USER_BASIC_INFO + userId);
            RedisUtil.getRedis().delete(Constants.USER_ACCOUNT_BASIC_INFO + user.getAccount());
        }
    }
}
