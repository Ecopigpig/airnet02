package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.mapper.CityMapper;
import com.zsc.servicedata.service.CityService;
import model.weather.AreaCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public int insertAreaCode(List<AreaCode> areaCodeList) {
        return cityMapper.insertAreaCode(areaCodeList);
    }

    @Override
    public AreaCode selectCodeByAreaName(String city,String area) {
        //先直接通过市名和城市内区域名称搜索,所有搜索不到就把区域名称设定为市名再次搜索
        AreaCode areaCode = cityMapper.selectCodeByAreaName(city,area);
        if(areaCode==null){
            areaCode = cityMapper.selectCodeByAreaName(city,city);
            if(areaCode==null) {
                return null;
            }
        }else {
            return areaCode;
        }
        return areaCode;
    }

}
