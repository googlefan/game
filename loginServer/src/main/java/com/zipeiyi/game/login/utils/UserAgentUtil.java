package com.zipeiyi.game.login.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhangxiaoqiang
 * @createTime 2013-05-10
 */
@Service
public class UserAgentUtil implements ApplicationContextAware {
    private static Map<String,String> uaMap;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        uaMap = (Map<String,String>) applicationContext.getBean("uaMap");
    }
    public static String guessUserAgent(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return "empty";
        }
        String lcUserAgent = StringUtils.lowerCase(userAgent);

        for(String key : uaMap.keySet()){
            if(lcUserAgent.contains(key)){
                return uaMap.get(key);
            }
        }
        return "unknown";
    }
}
