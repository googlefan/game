package com.zipeiyi.game.login.controller;

import com.zipeiyi.core.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by zhangxiaoqiang on 16/10/25.
 */
public abstract class WebController {

    /**
     * 解析SPRING MVC的PATH变量上下文，纯字符串对象
     * @param request Servlet 请求
     * @return 非null的不可修改Map
     */
    protected Map<String, String> getPathVariables(HttpServletRequest request) {
        Map<String, String> variables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (variables == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(variables);
    }

    /**
     * 获取参数值
     *
     * @param request
     *            Servlet 请求
     * @param key
     *            参数名称
     * @param defaultValue
     *            参数不存在时默认值
     * @return 非null参数值
     */
    protected String get(HttpServletRequest request, String key, String defaultValue) {
        return RequestUtils.getString(request, key, defaultValue);
    }

    protected int get(HttpServletRequest request, String key, int defaultValue) {
        return RequestUtils.getInt(request, key, defaultValue);
    }
    protected double get(HttpServletRequest request,String key,double defaultValue) {
        return RequestUtils.getDouble(request, key, defaultValue);
    }

    /**
     * 获取整数值
     *
     * @param request
     *            Servlet 请求
     * @param key
     *            参数名称
     * @param defaultValue
     *            默认值
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @return 参数值，如果比最小值min小，则返回min;如果比最大值max大，则返回max
     */
    protected int get(HttpServletRequest request, String key, int defaultValue, int min, int max) {
        int value = get(request, key, defaultValue);
        return Math.min(max, Math.max(min, value));
    }

    protected boolean get(HttpServletRequest request, String key, boolean defaultValue) {
        return RequestUtils.getBoolean(request, key, defaultValue);
    }

    protected long get(HttpServletRequest request, String key, long defaultValue) {
        return RequestUtils.getLong(request, key, defaultValue);
    }

    protected short get(HttpServletRequest request, String key, short defaultValue) {
        return RequestUtils.getShort(request, key, defaultValue);
    }

    protected String trimToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    protected String subString(String s, int start, int end) {
        return StringUtils.substring(s, start, end);
    }

    protected boolean isBlank(String s) {
        return StringUtils.isBlank(s);
    }

    protected boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    protected boolean isEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }
}
