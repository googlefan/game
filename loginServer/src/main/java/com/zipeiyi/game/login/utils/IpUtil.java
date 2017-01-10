package com.zipeiyi.game.login.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangxiaoqiang on 16/12/12.
 */
public class IpUtil {

    public static String getIP(HttpServletRequest request){
        String headerName = "X-Forwarded-For";
        String ip = request.getHeader(headerName);
        String result = null;

        if (StringUtils.isBlank(ip) || StringUtils.endsWithIgnoreCase("unknown", ip)) {
            result = request.getRemoteAddr();
        }
        else {
            String[] ips = ip.split(",");
            for (int i = ips.length - 1; i >= 0; --i) {
                ips[i] = StringUtils.trimToEmpty(ips[i]);
                String newip = ips[i];
                if (!newip.startsWith("211.") && !newip.startsWith("127.0.0.1") || newip.startsWith("211.151.")) {
                    result = ips[i];
                    break;
                }
            }
            if (result == null) {
                result = request.getRemoteAddr();
            }
        }
        result = StringUtils.trimToEmpty(result);
        return result;
    }

}
