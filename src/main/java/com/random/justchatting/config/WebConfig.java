package com.random.justchatting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/templates/", "classpath:/static/")
//                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "*:3000")
//                .allowedHeaders("Authorization", "Content-Type")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//    }
}
