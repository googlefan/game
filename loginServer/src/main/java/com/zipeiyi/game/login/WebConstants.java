package com.zipeiyi.game.login;

import com.zipeiyi.game.login.utils.PropertiesUtil;

import java.util.Map;

/**
 * Created by zhangxiaoqiang on 16/12/12.
 */
public interface WebConstants {

    /**响应状态码＋响应msg **/
    public static final int API_UN_KNOW_EXCEPTION = 8000;

    public static final int SUCCESS = 0;

    public static final int ACCOUNT_INPUT_ERROR = 1;

    public static final int NOT_LOGIN = 2;

    public static final int LOGIN_FREQUENCY_LIMIT = 3;

    public static final int ACCOUNT_FOMAT_ERROR = 4;

    public static final int USER_NOT_EXIST_ERROR = 5;

    public static final int USER_BLOCK_ERROR = 6;

    public static final int GATE_SERVER_CONNECT_ERROR = 7;

    public static final int API_REQUEST_ERROR = 8;

    public static final int REG_ERROR_CODE = 9;

    public static final int ACCOUNT_REGISTED_ERROR = 10;

    public static final int INPUT_PARAM_EMPTY_ERROR = 11;

    public static final int SEND_SMS_FREQUENCY_LIMIT = 12;

    public static final String NOT_LOGIN_MESSAGE = "还没有登陆，请登录";

    public static final String ACCOUNT_INPUT_ERROR_MESSAGE = "请输入用户名或者密码";

    public static final String ACCOUNT_IS_REGISTER = "账户已经注册，请注册个新的用户";

    public static final String LOGIN_FREQUENCY_LIMIT_ERORR_MESSAGE = "登陆过于频繁，请稍后重试";

    public static final String FETCH_SECURITY_CODE_LIMIT_ERROR_MESSAGE = "获取验证码过于频繁，请稍后再试";

    public static final String ACCOUNT_FOMAT_ERROR_MESSAGE = "帐号格式错误，请重新输入";

    public static final String USER_NOT_EXIST_ERROR_MESSAGE = "用户不存在，请注册后在登陆";

    public static final String USER_BLOCK_ERROR_MSG = "用户已封禁，详情咨询客服";

    public static final String GATE_SERVER_CONNECT_ERROR_MSG = "连接服务器异常，稍后重试";

    public static final String ACCOUNT_API_REQUEST_PARSE_ERROR = "数据验证异常，稍后重试";

    public static final String DEFAULT_USER_NAME = "用户";

    public static final String HTTP_REQUEST_EXCEPTION_MSG = "服务器请求异常，稍后重试";

    /** 配置文件加载内存 **/
    public final static Map<String,String> CONFIG= PropertiesUtil.getAllProperty("config.properties");

    /**
     * 网站登陆html页面
     */
    public static final String LOGIN_MAIN = "/game/login.html";

    /**
     * 网站第三方登陆成功后跳转到html页面，html中间页面获取token以及gateServer地址
     */
    public static final String LOGIN_SUCCESS = "/game/redirect.html";

    public static final String ERROR_INDEX_PAGE = "/game/";

    public static final String DOMAIN = "game.zipeiyi.com";

}
