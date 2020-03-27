package com.zsc.servicehi.controller;

import com.alibaba.fastjson.JSON;
import com.zsc.servicehi.utils.GetWeatherData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.result.ResponseResult;
import model.weather.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(value = "WeatherController", tags = "天气控制器")
@RestController
@RequestMapping("/weather")
@EnableCaching
public class WeatherController {

    private final static Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //这个链接能拿到数据
    @ApiOperation(value = "根据城市中文名获取该城市的未来24小时天气情况,专供服务调用,目前是实时获取")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/get24HourData",method = {RequestMethod.POST})
    public List<Weather24Hours> get24HourData(@RequestBody Map<String, String> map) {
        GetWeatherData getWeatherData = new GetWeatherData();
        List<Weather24Hours> weather24HoursList = new ArrayList<>();
        String city = map.get("city");
        try {
            weather24HoursList = getWeatherData.get24HourWeather(city);
        }catch (Exception e){
            logger.error("/weather/get24HourData异常:",e);
        }
        return weather24HoursList;
    }


    @ApiOperation(value = "根据城市中文名获取该城市的所有地区代码,专供服务调用,目前是实时获取")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称,如:广州", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAreaCode",method = {RequestMethod.POST})
    public List<AreaCode> getAreaCode(@RequestBody Map<String, String> map) {
        GetWeatherData getWeatherData = new GetWeatherData();
        String city = map.get("city");
        List<AreaCode> areaCodeList = new ArrayList<>();
        try {
            areaCodeList = getWeatherData.getAreaCode(city);
        }catch (Exception e){
            logger.error("/weather/getAreaCode异常:",e);
        }
        return areaCodeList;
    }

    @ApiOperation(value = "根据城市的区号和邮政编号获该城市的实时天气,专供服务调用")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "areaCode", value = "城市区号，如:0763", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "postalCode", value = "城市邮政编码6位数字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getInstanceWeather",method = {RequestMethod.POST})
    public InstanceWeather getInstanceWeather(@RequestBody CityCode cityCode) {
        String areaCode = cityCode.getAreaCode();
        String postalCode = cityCode.getPostalCode();
        GetWeatherData getWeatherData = new GetWeatherData();
        InstanceWeather instanceWeather = new InstanceWeather();
        try {
            instanceWeather = getWeatherData.getInstanceTimeWeather(areaCode, postalCode);
        }catch (Exception e){
            logger.error("/weather/getInstanceWeather异常:",e);
        }
        return instanceWeather;
    }

    @ApiOperation(value = "根据城市中文名获取该城市的未来24小时天气情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/get24Hour",method = RequestMethod.POST)
    public ResponseResult get24Hour(@RequestBody Map<String, String> map) {
        GetWeatherData getWeatherData = new GetWeatherData();
        List<Weather24Hours> weather24HoursList = new ArrayList<>();
        List<Weather24Hours> redisList = new ArrayList<>();
        //每次响应都要去redis看看有没有这个value可以去取
        String city = map.get("city");
        String key = city + "24HourWeather";
        Long length = redisTemplate.opsForList().size(key);
        try {
            for (Long i = 0L; i < length; i++) {
                JSON json = (JSON) JSON.toJSON(redisTemplate.opsForList().index(key, i));
                Object javaObject = JSON.toJavaObject(json, Weather24Hours.class);
                Weather24Hours result = new Weather24Hours();
                BeanUtils.copyProperties(javaObject, result);
                redisList.add(result);
            }
        }catch (Exception e){
            logger.error("/weather/get24Hour循环异常:",e);
        }
        if (redisList.size() == 0) {
            //没有缓存就存进去
            weather24HoursList = getWeatherData.get24HourWeather(city);
            weather24HoursList.forEach(item -> {
                redisTemplate.opsForList().rightPush(key, item);
            });
        } else {
            //有缓存就取出来
            weather24HoursList.addAll(redisList);
        }
        stringRedisTemplate.expire(key,30,TimeUnit.MINUTES);
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        if (weather24HoursList != null) {
            result.setMsg(true);
            result.setTotal(Long.valueOf(weather24HoursList.size()));
        }
        result.setData(weather24HoursList);
        return result;
    }

    @ApiOperation(value = "根据城市中文名获取该城市的未来15天的天气情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getIn15Days",method = RequestMethod.POST)
    public ResponseResult getIn15Days(@RequestBody Map<String, String> map) {
        GetWeatherData getWeatherData = new GetWeatherData();
        String city = map.get("city");
        List<WeatherIn15Days> weatherIn15DaysList = getWeatherData.getWeatherIn15Days(city);
        List<WeatherIn15Days> redisList = new ArrayList<>();
        //每次响应都要去redis看看有没有这个value可以去取
        String key = city + "15DayWeather";
        Long length = redisTemplate.opsForList().size(key);
        try {
            for (Long i = 0L; i < length; i++) {
                JSON json = (JSON) JSON.toJSON(redisTemplate.opsForList().index(key, i));
                Object javaObject = JSON.toJavaObject(json, WeatherIn15Days.class);
                WeatherIn15Days result = new WeatherIn15Days();
                BeanUtils.copyProperties(javaObject, result);
                redisList.add(result);
            }
        }catch (Exception e){
            logger.error("/weather/getIn15Days循环异常",e);
        }
        if (redisList.size() == 0) {
            //没有缓存就存进去
            weatherIn15DaysList = getWeatherData.getWeatherIn15Days(city);
            weatherIn15DaysList.forEach(item -> {
                redisTemplate.opsForList().rightPush(key, item);
            });
        } else {
            //有缓存就取出来
            weatherIn15DaysList.addAll(redisList);
        }
        stringRedisTemplate.expire(key,30,TimeUnit.MINUTES);

        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        if (weatherIn15DaysList != null) {
            result.setMsg(true);
        }
        result.setData(weatherIn15DaysList);
        return result;
    }


}
