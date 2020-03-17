package com.zsc.servicedata.service.feign;

import model.pollutant.MonitorSite;
import model.weather.AreaCode;
import model.weather.InstanceWeather;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-hi",url = "http://39.108.10.105:8763")
@Repository
public interface HiFeignService {

    @PostMapping("/pollutant/offerNationPollutant")                        //此处名字需要和01服务的controller路径保持一致
    String offerNationPollutant();

    @PostMapping("/weather/getAreaCode")
    List<AreaCode> getAreaCode(@RequestParam String city);

    @PostMapping("/weather/getInstanceWeather")
    InstanceWeather getInstanceWeather(@RequestParam String areaCode,@RequestParam String postalCode);

    @PostMapping("/pollutant/offerSitesWithLocation")
    List<MonitorSite> getSitesWithLocation();
}
