package org.springboot.example.example.webmvc.config;

import org.springboot.example.example.webmvc.interceptor.CustomerHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot MVC 拓展功能（拦截器，格式化程序，视图控制器和其他功能）
 */
@Configuration
public class ExtractMvcConfig implements WebMvcConfigurer {

    /**
     * 添加视图控制器
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/index.html").setViewName("login");
         registry.addViewController("/").setViewName("login");
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry
                .addInterceptor(new CustomerHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/index.html",
                        "/",
                        "/user/login",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.svg"
                );

    }


}
