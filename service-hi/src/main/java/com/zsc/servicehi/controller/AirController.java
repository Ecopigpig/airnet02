package com.zsc.servicehi.controller;

import com.alibaba.fastjson.JSON;
import com.zsc.servicehi.utils.GetAirData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.air.AirQuality;
import model.result.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Api(value = "AirController", tags = "空气质量控制器")
@RestController
@RequestMapping("/air")
public class AirController {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "根据城市中文名获取该城市的空气质量情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称,一定要带市字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAirQuality", method = RequestMethod.GET)
    public ResponseResult getAirQuality(@RequestParam String city) {
        GetAirData airData = new GetAirData();
        AirQuality airQuality = new AirQuality();
        String key = city + "AirQuality";
        JSON json = (JSON) JSON.toJSON(redisTemplate.opsForValue().get(key));
        Object javaObject = JSON.toJavaObject(json, AirQuality.class);
        if (javaObject == null) {
            //没有缓存就存进去
            airQuality = airData.getAirQuality(city);
            redisTemplate.opsForValue().set(key, airQuality);
        } else {
            //有缓存就取出来
            BeanUtils.copyProperties(javaObject, airQuality);
        }
        stringRedisTemplate.expire(key, 30, TimeUnit.MINUTES);
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        if (airQuality != null) {
            result.setMsg(true);
        }
        result.setData(airQuality);
        return result;
    }

}
