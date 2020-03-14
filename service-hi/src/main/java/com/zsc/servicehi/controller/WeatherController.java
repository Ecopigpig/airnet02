package com.zsc.servicehi.controller;

import com.alibaba.fastjson.JSON;
import com.zsc.servicehi.utils.GetWeatherData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.result.ResponseResult;
import model.weather.AreaCode;
import model.weather.InstanceWeather;
import model.weather.Weather24Hours;
import model.weather.WeatherIn15Days;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(value = "WeatherController", tags = "天气控制器")
@RestController
@RequestMapping("/weather")
@EnableCaching
public class WeatherController {


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //这个链接能拿到数据
    @ApiOperation(value = "根据城市中文名获取该城市的未来24小时天气情况,专供服务调用")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/get24HourData",method = {RequestMethod.GET,RequestMethod.POST})
    public List<Weather24Hours> get24HourData(@RequestParam String city) {
        GetWeatherData getWeatherData = new GetWeatherData();
        List<Weather24Hours> weather24HoursList = getWeatherData.get24HourWeather(city);
        return weather24HoursList;
    }


    @ApiOperation(value = "根据城市中文名获取该城市的所有地区代码,专供服务调用")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称,如:广州", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAreaCode",method = {RequestMethod.GET,RequestMethod.POST})
    public List<AreaCode> getAreaCode(@RequestParam String city) {
        GetWeatherData getWeatherData = new GetWeatherData();
        List<AreaCode> areaCodeList = getWeatherData.getAreaCode(city);
        return areaCodeList;
    }

    @ApiOperation(value = "根据城市的区号和邮政编号获该城市的实时天气,专供服务调用")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "areaCode", value = "城市区号，如:0763", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "postalCode", value = "城市邮政编码6位数字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getInstanceWeather",method = {RequestMethod.GET,RequestMethod.POST})
    public InstanceWeather getInstanceWeather(@RequestParam String areaCode, @RequestParam String postalCode) {
        GetWeatherData getWeatherData = new GetWeatherData();
        InstanceWeather instanceWeather = getWeatherData.getInstanceTimeWeather(areaCode,postalCode);
        return instanceWeather;
    }

    @ApiOperation(value = "根据城市中文名获取该城市的未来24小时天气情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/get24Hour",method = RequestMethod.GET)
    public ResponseResult get24Hour(@RequestParam String city) {
        GetWeatherData getWeatherData = new GetWeatherData();
        List<Weather24Hours> weather24HoursList = new ArrayList<>();
        List<Weather24Hours> redisList = new ArrayList<>();
        //每次响应都要去redis看看有没有这个value可以去取
        String key = city + "24HourWeather";
        Long length = redisTemplate.opsForList().size(key);
        for (Long i = 0L; i < length; i++) {
            JSON json = (JSON) JSON.toJSON(redisTemplate.opsForList().index(key, i));
            Object javaObject = JSON.toJavaObject(json, Weather24Hours.class);
            Weather24Hours result = new Weather24Hours();
            BeanUtils.copyProperties(javaObject, result);
            redisList.add(result);
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
    @RequestMapping(value = "/getIn15Days",method = RequestMethod.GET)
    public ResponseResult getIn15Days(@RequestParam String city) {
        GetWeatherData getWeatherData = new GetWeatherData();
        List<WeatherIn15Days> weatherIn15DaysList = getWeatherData.getWeatherIn15Days(city);
        List<WeatherIn15Days> redisList = new ArrayList<>();
        //每次响应都要去redis看看有没有这个value可以去取
        String key = city + "15DayWeather";
        Long length = redisTemplate.opsForList().size(key);
        for (Long i = 0L; i < length; i++) {
            JSON json = (JSON) JSON.toJSON(redisTemplate.opsForList().index(key, i));
            Object javaObject = JSON.toJavaObject(json, WeatherIn15Days.class);
            WeatherIn15Days result = new WeatherIn15Days();
            BeanUtils.copyProperties(javaObject, result);
            redisList.add(result);
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
