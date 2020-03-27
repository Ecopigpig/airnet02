package com.zsc.servicehi.controller;

import com.alibaba.fastjson.JSON;
import com.zsc.servicehi.utils.GetAirData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.air.AirQuality;
import model.result.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(value = "AirController", tags = "空气质量控制器")
@RestController
@RequestMapping("/air")
public class AirController {

    private final static Logger logger = LoggerFactory.getLogger(AirController.class);

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "根据城市中文名获取该城市的空气质量情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称,一定要带市字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAirQuality", method = RequestMethod.POST)
    public ResponseResult getAirQuality(@RequestBody Map<String, String> map){
        GetAirData airData = new GetAirData();
        AirQuality airQuality = new AirQuality();
        String city = map.get("city");
        String key = city + "AirQuality";
        JSON json = null;
        try {
            json = (JSON) JSON.toJSON(redisTemplate.opsForValue().get(key));
        }catch (Exception e){
            logger.error("/air/getAirQuality异常:",e);
        }
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
