package com.run.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置全局跨域配置，也可以在controller层使用@CrossOrigin注解来进行特殊配置
 * 见29.1.13 CORS Support部分：https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/#using-boot-auto-configuration
 */
@Component
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("*").//非Cors属性 配置支持跨域的路径
                        //可见：https://cloud.tencent.com/developer/section/1189897
                        allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH").//Access-Control-Allow-Methods 配置支持跨域请求的方法,
                        allowedHeaders("*").//Access-Control-Request-Headers 配置允许的自定义请求头
                        allowedOrigins("*").//Access-Control-Allow-Origin 配置允许的源
                        allowCredentials(true).//Access-Control-Allow-Credentials 配置是否允许发送Cookie
                        exposedHeaders("content-type").//Access-Control-Expose-Headers 配置响应的头信息,
                        // 包括：Cache-Control、Content-Language、Content-Type、Expires、Last-Modified、Pragma
                        maxAge(1000);//Access-Control-Max-Age 配置预检请求的有效时间
            }
        };
    }
}
