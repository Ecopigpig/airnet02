package com.zsc.servicedata.service;

import model.weather.AreaCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityService {

    /**
     * 新增实时天气城市代码
     * @param areaCodeList
     */
    int insertAreaCode(List<AreaCode> areaCodeList);

    /**
     * 根据城市名称查询其对应的代码
     * @param city
     * @param area
     * @return
     */
    AreaCode selectCodeByAreaName(String city, String area);
}
