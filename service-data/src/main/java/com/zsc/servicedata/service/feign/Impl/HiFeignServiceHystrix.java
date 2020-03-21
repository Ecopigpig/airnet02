package com.zsc.servicedata.service.feign.Impl;

import com.zsc.servicedata.service.feign.HiFeignService;
import model.pollutant.MonitorSite;
import model.weather.AreaCode;
import model.weather.InstanceWeather;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HiFeignServiceHystrix implements HiFeignService {
    @Override
    public String offerNationPollutant() {
        return "很抱歉,当前无法获取全国污染排行榜";
    }

    @Override
    public List<AreaCode> getAreaCode(String city) {
        List<AreaCode> areaCodeList = new ArrayList<>();
        return areaCodeList;
    }

    @Override
    public InstanceWeather getInstanceWeather(String areaCode, String postalCode) {
        InstanceWeather instanceWeather = new InstanceWeather();
        return instanceWeather;
    }

    @Override
    public List<MonitorSite> getSitesWithLocation() {
        List<MonitorSite> monitorSiteList = new ArrayList<>();
        return monitorSiteList;
    }
}
