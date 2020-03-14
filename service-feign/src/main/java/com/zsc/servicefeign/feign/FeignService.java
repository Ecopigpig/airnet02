package com.zsc.servicefeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "localhost:8763")
public interface FeignService {
    @GetMapping("/pollutant/offerNationPollutant")                        //此处名字需要和01服务的controller路径保持一致
    String microService01FeignTest();    //参数一定一定一定要加@RequestParam注解!!!
}
