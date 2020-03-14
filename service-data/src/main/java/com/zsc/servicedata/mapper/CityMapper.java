package com.zsc.servicedata.mapper;

import model.pollutant.MonitorSite;
import model.weather.AreaCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CityMapper {

//    @Insert("insert into citysite () values ()")
    void saveSiteOfCity(@Param("list") List<MonitorSite> monitorSiteList);

    int insertAreaCode(@Param("list") List<AreaCode> areaCodeList);

    AreaCode selectCodeByAreaName(String city, String area);

    List<MonitorSite> selectMonitorSiteByCity(@Param("city") String city);
}
