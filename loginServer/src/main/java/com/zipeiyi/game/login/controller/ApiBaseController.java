package com.zipeiyi.game.login.controller;

import com.zipeiyi.core.common.utils.RequestUtils;
import com.zipeiyi.game.login.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * Created by zhangxiaoqiang on 16/6/6.
 */
public abstract class ApiBaseController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ApiBaseController.class);

    public static final Logger errorLogger = LoggerFactory.getLogger("error");

    @ExceptionHandler
    public void handleException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception ex) {
        try {
            errorLogger.error("undefine error,requestDump:{},stackTrace:{}", dumpRequst(request), getExceptionString(ex, get(request, "printStackTrace", true)));
            RequestUtils.writeResponse(request, response, toJson(WebConstants.API_UN_KNOW_EXCEPTION, getExceptionString(ex, get(request, "printStackTrace", false))));

        } catch (Throwable e) {
            errorLogger.error("main handle exception throws exception", e);
        }
    }

    private String dumpRequst(HttpServletRequest request) throws Exception {
        Map<String, Object> r = new LinkedHashMap<>();

        r.put("method", request.getMethod());
        r.put("url", request.getRequestURL());
        r.put("params", request.getParameterMap());
        r.put("remoteAddr", request.getRemoteAddr());

        Map<String, Object> header = new HashMap<>();
        r.put("header", header);
        Enumeration<String> headerIt = request.getHeaderNames();
        while (headerIt.hasMoreElements()) {
            String name = headerIt.nextElement();
            Enumeration<String> it = request.getHeaders(name);
            List<String> vs = new ArrayList<>();
            while (it.hasMoreElements()) {
                vs.add(it.nextElement());
            }
            header.put(name, vs.size()==1 ? vs.get(0) : vs);
        }


        return toJson(WebConstants.API_UN_KNOW_EXCEPTION, "api请求错误", r);

    }

    private String getExceptionString(Throwable ex, boolean printStackTrace) throws Exception {
        if (!printStackTrace) {
            return ex.getMessage();
        }
        try (ByteArrayOutputStream buf = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(buf)) {
            ex.printStackTrace(ps);
            ps.flush();
            buf.flush();
            return new String(buf.toByteArray(), "UTF-8");
        }
    }
}
