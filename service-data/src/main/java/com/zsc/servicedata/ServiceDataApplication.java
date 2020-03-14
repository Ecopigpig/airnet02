package com.zsc.servicedata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@RestController
@EnableHystrix  //开启断路器
@EnableHystrixDashboard
@EnableCircuitBreaker
@SpringBootApplication
@MapperScan("com.zsc.servicedata.mapper")
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zsc.servicedata.service.feign"})
//@ServletComponentScan
//@ComponentScan(basePackages={"com.zsc.servicedata.config","com.zsc.servicedata.service",
//        "com.zsc.servicedata.service.Impl","com.zsc.servicedata.tag"})
public class ServiceDataApplication {

//    @Bean
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        //防止超时
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        //建立连接所用的时间 5s
        simpleClientHttpRequestFactory.setConnectTimeout(5000);
        //服务器读取可用资源的时间 10s
        simpleClientHttpRequestFactory.setReadTimeout(600000);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceDataApplication.class, args);
    }

}
