package com.zsc.servicedata.service.feign;

import com.zsc.servicedata.service.feign.Impl.HiFeignServiceHystrix;
import model.pollutant.MonitorSite;
import model.weather.AreaCode;
import model.weather.CityCode;
import model.weather.InstanceWeather;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "service-hi",url = "http://39.108.10.105:8763",fallback = HiFeignServiceHystrix.class)
@Repository
public interface HiFeignService {

    @PostMapping("/pollutant/offerNationPollutant")
    String offerNationPollutant();

    @PostMapping("/weather/getAreaCode")
    List<AreaCode> getAreaCode(@RequestParam String city);

    @PostMapping("/weather/getInstanceWeather")
    InstanceWeather getInstanceWeather(@RequestBody CityCode cityCode);

    @PostMapping("/pollutant/offerSitesWithLocation")
    List<MonitorSite> getSitesWithLocation();
}
