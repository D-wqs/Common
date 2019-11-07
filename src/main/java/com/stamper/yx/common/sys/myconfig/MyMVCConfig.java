package com.stamper.yx.common.sys.myconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MyMVCConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index.html").setViewName("login");
        registry.addViewController("/yunxi/main.html").setViewName("dashboard");
        registry.addViewController("/yunxi/debugging").setViewName("debugging/debugging");
        registry.addViewController("/error.html").setViewName("error/5xx.html");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/user/**")
//                        .excludePathPatterns("/index.html","/","/user/login","/static/**","/asserts/**");
//        //再加一个registry.addInterceptor
//    }
    //    https://www.cnblogs.com/XtsLife/p/10488575.html
}
