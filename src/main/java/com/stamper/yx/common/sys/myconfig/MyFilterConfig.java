package com.stamper.yx.common.sys.myconfig;

import com.stamper.yx.common.sys.myconfig.filter.SignetFilter;
import com.stamper.yx.common.sys.myconfig.filter.YXFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyFilterConfig {

    /**
     * 过滤器注册
     */

    @Autowired
    private YXFilter yxFilter;
    @Autowired
    private SignetFilter signetFilter;

    @Bean
    public FilterRegistrationBean userRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(yxFilter);
        registrationBean.addUrlPatterns("/yunxi/*");//处理页面跳转的过滤器
        registrationBean.setName("yxFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean signetRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(signetFilter);
        registrationBean.addUrlPatterns("/device/*");
        registrationBean.setName("signetFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
