package com.zsc.servicedata.config;

import com.zsc.servicedata.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //这得的login用两种方式，一种像本文一样用注入的方式，第二种是用new AuthenticationInterceptor ()；但是这用new 的方式的话，在LoginInterceptor 种注入service层会不起作用。所以用注入的方式。
        registry.addInterceptor(authenticationInterceptor())
                //添加需要拦截的地址，静态文件也可以再这里做配置，用excludePathPatterns是不拦截
                .addPathPatterns("/**");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

}
