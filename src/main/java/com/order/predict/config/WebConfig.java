package com.order.predict.config;

import com.order.predict.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/login.html", "/login", "/error",
                        "/css/**", "/js/**", "/images/**" // 静态资源不拦截
                );
    }
}
