package com.zsc.servicedata.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service-hi")
@Repository
public interface HiFeignService {

    @GetMapping("/pollutant/offerNationPollutant")                        //此处名字需要和01服务的controller路径保持一致
    String offerNationPollutant();    //参数一定一定一定要加@RequestParam注解!!!

}
