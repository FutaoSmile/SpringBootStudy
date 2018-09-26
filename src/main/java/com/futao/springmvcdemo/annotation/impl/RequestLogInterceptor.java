package com.futao.springmvcdemo.annotation.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.futao.springmvcdemo.utils.RequestUtils.getCookies;
import static com.futao.springmvcdemo.utils.RequestUtils.getSessionParameters;

/**
 * @author futao
 * Created on 2018/9/20-12:12.
 * 请求controller记录日志
 */
@Component
public class RequestLogInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            RestController restController = ((HandlerMethod) handler).getMethod().getDeclaringClass().getAnnotation(RestController.class);
            if (ObjectUtils.allNotNull(restController)) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n")
                  .append("请求地址: " + request.getRequestURL())
                  .append("\n")
                  .append("请求sessions: " + getSessionParameters(request.getSession(false)))
                  .append("\n")
                  .append("请求参数：" + queryString(request.getQueryString()))
                  .append("\n")
                  .append("请求cookies: " + getCookies(request.getCookies()));
                logger.info(String.valueOf(sb));
            }
        }
        return true;
    }

    private String queryString(String qStr) throws UnsupportedEncodingException {
        return qStr == null ? "" : URLDecoder.decode(qStr, "UTF-8");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}