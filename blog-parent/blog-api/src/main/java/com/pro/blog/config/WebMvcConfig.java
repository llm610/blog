package com.pro.blog.config;

import com.pro.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置：因为前端访问域名和后端访问域名不一致
        //域名就是IP加端口
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    //配置玩拦截器后需要添加到webmvc内

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/test")
                               .addPathPatterns("/comments/create/change").addPathPatterns("/articles/publish");
    }
}
