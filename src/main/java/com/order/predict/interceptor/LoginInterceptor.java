package com.order.predict.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 放行登录页面和静态资源
        String uri = request.getRequestURI();
        if (uri.endsWith("/login.html") || uri.endsWith("/login") || uri.contains("/css/") || uri.contains("/js/")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return true; // 已登录，放行
        }

        // 未登录，重定向到登录页
        response.sendRedirect("/login.html");
        return false;
    }
}
