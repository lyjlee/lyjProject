package com.lyj.portfolio.config;

import com.lyj.portfolio.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //외부
        registry.addResourceHandler("/summernoteImage/**")
                .addResourceLocations("file:///C:/summernote_image/");
    }

    //추가 안해도 해결
//    @Bean
//    public FilterRegistrationBean filterBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new MyFilter());
//        registrationBean.setOrder(Integer.MIN_VALUE);
//        registrationBean.addUrlPatterns("/*");
//
//        return registrationBean;
//    }

}
