package com.zipeiyi.game.login.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoqiang on 16/12/15.
 */
public class WebchatConnectUtil {

    private static final String APPID = "101048468";

    private static final String APPKEY = "1aae1997a4afcce8ab3ca54bec212e5d";

    private static final Logger logger = Logger.getLogger(WebchatConnectUtil.class);

    public static final String W_NAME = "_weixin_";

    public static final String REDIRECT_URI = "https://login.game.zipeiyi.com/connect/weixinLogin";


    public static String makeRequestURL(String origURL) throws UnsupportedEncodingException {
        String requestURL = "https://open.weixin.qq.com/connect/qrconnect?";

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("response_type", "code"));
        list.add(new BasicNameValuePair("appid", APPID));

        if (StringUtils.isBlank(origURL)) {
            origURL = "https://login.game.zipeiyi.com";
        }

        list.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI + "?origURL="
                + URLEncoder.encode(origURL, HTTP.UTF_8)));

        list.add(new BasicNameValuePair("scope", "snsapi_login"));

        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);

        return requestURL + paramStr;
    }


    public static WebchatToken getAccessTokenAndOpenId(String code)
            throws UnsupportedEncodingException {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
        list.add(new BasicNameValuePair("appid", APPID));
        list.add(new BasicNameValuePair("secret", APPKEY));
        list.add(new BasicNameValuePair("code", code));


        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/oauth2/access_token?" + paramStr);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            byte[] bytes = new byte[256];
            StringBuilder sb = new StringBuilder();
            while (is.read(bytes) > 0) {
                sb.append(new String(bytes));
                bytes = new byte[256];
            }

            if (StringUtils.isBlank(sb.toString())) {
                return null;
            }

            logger.info("getAccessToken : " + sb.toString());

            JSONObject json = new JSONObject(sb.toString());
            WebchatToken webchatToken = new WebchatToken();
            webchatToken.token = json.getString("access_token");
            webchatToken.openId = json.getString("openid");
            return webchatToken;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpGet.abort();
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    public static WebchatUser getUserName(String accessToken, String openId) {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("access_token", accessToken));
        list.add(new BasicNameValuePair("openid", openId));


        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/userinfo?" + paramStr);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            byte[] bytes = new byte[256];
            StringBuffer sb = new StringBuffer();
            while (is.read(bytes) > 0) {
                sb.append(new String(bytes));
                bytes = new byte[256];
            }

            if (StringUtils.isBlank(sb.toString())) {
                return null;
            }

            String openIdResp = sb.toString();

            logger.info("getUserName : " + openIdResp);
            JSONObject json = new JSONObject(openIdResp);
            WebchatUser webchatUser = new WebchatUser();
            webchatUser.nickName = json.getString("nickname");
            webchatUser.city = json.getString("city");
            webchatUser.country = json.getString("country");
            webchatUser.province = json.getString("province");
            webchatUser.sex = json.getInt("sex");
            webchatUser.headimgurl = json.getString("headimgurl");
            return webchatUser;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpGet.abort();
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    public static class WebchatToken{
       public String openId;
       public String token;
    }

    public static class WebchatUser{
        public String nickName;
        public String province;
        public String city;
        public String country;
        public int sex;
        public String headimgurl;
    }
}
