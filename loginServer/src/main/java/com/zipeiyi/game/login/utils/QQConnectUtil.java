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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangxiaoqiang on 16/12/12.
 */
public class QQConnectUtil {

    private static final String APPID = "101048468";

    private static final String APPKEY = "1aae1997a4afcce8ab3ca54bec212e5d";

    private static final Logger logger = Logger.getLogger(QQConnectUtil.class);

    public static final String REDIRECT_URI = "https://login.game.zipeiyi.com/connect/qqLogin";

    public static final String Q_NAME = "_qq_";

    public static final Pattern pattern = Pattern.compile(".*(\\{.*?\\}).*");


    public static String makeRequestURL(String origURL) throws UnsupportedEncodingException {
        String requestURL = "https://graph.qq.com/oauth2.0/authorize";

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("response_type", "code"));
        list.add(new BasicNameValuePair("client_id", APPID));

        if (StringUtils.isBlank(origURL)) {
            origURL = "https://login.game.zipeiyi.com";
        }

        list.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI + "?origURL="
                + URLEncoder.encode(origURL, HTTP.UTF_8)));

        list.add(new BasicNameValuePair("state",LoginTools.getRandString(16)));
        list.add(new BasicNameValuePair("scope", "get_user_info"));

        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);

        return requestURL + "?" + paramStr;
    }

    public static String getOpenId(String accessToken) {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("access_token", accessToken));

        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://graph.qq.com/oauth2.0/me?" + paramStr);
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
                return "";
            }

            String openIdResp = sb.toString();

            logger.info("getOpenId : " + openIdResp);
            Matcher matcher = pattern.matcher(StringUtils.trim(openIdResp));
            if (matcher.matches()) {
                openIdResp = matcher.group(1);
                JSONObject json = new JSONObject(openIdResp);
                return json.getString("openid");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpGet.abort();
            httpClient.getConnectionManager().shutdown();
        }
        return "";
    }

    public static String getAccessToken(String code, String origURL)
            throws UnsupportedEncodingException {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
        list.add(new BasicNameValuePair("client_id", APPID));
        list.add(new BasicNameValuePair("client_secret", APPKEY));
        list.add(new BasicNameValuePair("code", code));
        list.add(new BasicNameValuePair("state", LoginTools.getRandString(16)));
        list.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI + "?origURL="
                + URLEncoder.encode(origURL, HTTP.UTF_8)));


        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://graph.qq.com/oauth2.0/token?" + paramStr);
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
                return "";
            }

            logger.info("getAccessToken : " + sb.toString());

            Map<String, String> paramMap = analysis(sb.toString());
            return paramMap.get("access_token");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpGet.abort();
            httpClient.getConnectionManager().shutdown();
        }
        return "";
    }

    public static Map<String, String> analysis(String url) {

        Map<String, String> paramMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(url)) {// 如果URL不是空字符串
            url = url.substring(url.indexOf('?') + 1);
            String paramaters[] = url.split("&");
            for (String param : paramaters) {
                String values[] = param.split("=");
                if (values.length > 1) {
                    paramMap.put(values[0], values[1]);
                }
            }
        }
        return paramMap;
    }

    public static String getUserName(String accessToken, String openId) {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("access_token", accessToken));
        list.add(new BasicNameValuePair("oauth_consumer_key", APPID));
        list.add(new BasicNameValuePair("openid", openId));
//        list.add(new BasicNameValuePair("format", "json"));


        String paramStr = URLEncodedUtils.format(list, HTTP.UTF_8);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://graph.qq.com/user/get_user_info?" + paramStr);
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
                return "";
            }

            String openIdResp = sb.toString();

            logger.info("getUserName : " + openIdResp);
            JSONObject json = new JSONObject(openIdResp);
            return json.getString("nickname");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpGet.abort();
            httpClient.getConnectionManager().shutdown();
        }
        return "";
    }

    public static void main(String[] args) throws ParseException {
        String r = "callback( {\"client_id\":\"YOUR_APPID\",\"openid\":\"YOUR_OPENID\"} )";
        Matcher matcher = pattern.matcher(StringUtils.trim(r));
        if (matcher.matches()) {
            r = matcher.group(1);
            JSONObject json = new JSONObject(r);
            String openid = json.getString("openid");
            System.out.println("openid=" + openid);
        }
    }

}
