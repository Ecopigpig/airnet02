package com.zsc.servicedata.service.feign;

import model.weather.AreaCode;
import model.weather.InstanceWeather;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-hi")
@Repository
public interface HiFeignService {

    @GetMapping("/pollutant/offerNationPollutant")                        //此处名字需要和01服务的controller路径保持一致
    String offerNationPollutant();

    @GetMapping("/weather/getAreaCode")
    List<AreaCode> getAreaCode(@RequestParam String city);

    @GetMapping("/weather/getInstanceWeather")
    InstanceWeather getInstanceWeather(@RequestParam String areaCode,@RequestParam String postalCode);
}
