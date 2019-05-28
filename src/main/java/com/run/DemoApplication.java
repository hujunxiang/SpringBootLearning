package com.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableCaching //开启缓存功能
@EnableRedisHttpSession//开启spring session 在redis中的缓存功能
public class DemoApplication extends SpringBootServletInitializer {

    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(DemoApplication.class);
    }

    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(DemoApplication.class);
//        app.setBannerMode(Banner.Mode.OFF);//关闭banner
//        app.run(args);
      SpringApplication.run(DemoApplication.class, args);
//        new SpringApplicationBuilder(DemoApplication.class)
//                .bannerMode(Banner.Mode.OFF)//关闭banner
//                .run(args);
    }

}
