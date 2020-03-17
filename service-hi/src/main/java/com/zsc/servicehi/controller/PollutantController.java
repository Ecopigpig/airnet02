package com.zsc.servicehi.controller;

import com.alibaba.fastjson.JSON;
import com.zsc.servicehi.utils.GetPollutantData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.air.AirQuality;
import model.pollutant.MonitorSite;
import model.pollutant.PollutantCity;
import model.pollutant.PollutionSite;
import model.result.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(value = "PollutantController", tags = "空气污染情况控制器")
@RestController
@RequestMapping("/pollutant")
public class PollutantController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 提供给service-data获取城市监测点名称列表,此接口仅提供名称
     *
     * @param city
     * @return
     */
    @ApiOperation(value = "通过城市名称获取该城市下的检测点名称,专供服务调用")
    @RequestMapping(value = "/offerPollutantSites", method = RequestMethod.POST)
    public Map<String, List<String>> offerPollutantSites(@RequestParam String city) {
        GetPollutantData getPollutantData = new GetPollutantData();
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        PollutantCity pollutantCity = new PollutantCity();
        String key = city + "PollutantSites";
        JSON json = (JSON) JSON.toJSON(redisTemplate.opsForValue().get(key));
        Object javaObject = JSON.toJavaObject(json, PollutantCity.class);
        if (javaObject == null) {
            //没有缓存就存进去
            pollutantCity = getPollutantData.getCityPollutionEpisode(city);
            redisTemplate.opsForValue().set(key, pollutantCity);
        } else {
            //有缓存就取出来
            BeanUtils.copyProperties(javaObject, pollutantCity);
        }
        List<String> cityList = new ArrayList<>();
        cityList.add(city);
        map.put("city", cityList);
        stringRedisTemplate.expire(key, 30, TimeUnit.MINUTES);
        List<PollutionSite> pollutionSiteList = pollutantCity.getPollutionSiteList();
        if (pollutionSiteList != null) {
            pollutionSiteList.forEach(site -> {
                list.add(site.getSiteName());
            });
            map.put("siteName", list);
        }
        return map;
    }


    @ApiOperation(value = "获取全国城市下的检测点名称包括其经纬度,专供服务调用")
    @RequestMapping(value = "/offerSitesWithLocation", method = RequestMethod.POST)
    public List<MonitorSite> offerSitesWithLocation() {
        GetPollutantData getPollutantData = new GetPollutantData();
        List<MonitorSite> list = getPollutantData.getSitesWithLocation();
        return list;
    }

    //直接获取实时的数据吧，不要经过缓存了
    @ApiOperation(value = "全国污染排行榜,专供服务调用")
    @RequestMapping(value = "/offerNationPollutant", method = RequestMethod.POST)
    public List<PollutantCity> offerNationPollutant() {
        GetPollutantData getPollutantData = new GetPollutantData();
        List<PollutantCity> pollutantCityList = getPollutantData.getNationPollutantRank();
        return pollutantCityList;
    }


    @ApiOperation(value = "根据城市中文名获取该城市的空气污染情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getCity", method = RequestMethod.POST)
    public ResponseResult getCity(@RequestParam String city) {
        GetPollutantData getPollutantData = new GetPollutantData();
        PollutantCity pollutantCity = new PollutantCity();
        String key = city + "PollutionSituation";
        JSON json = (JSON) JSON.toJSON(redisTemplate.opsForValue().get(key));
        Object javaObject = JSON.toJavaObject(json, PollutantCity.class);
        if (javaObject == null) {
            //没有缓存就存进去
            pollutantCity = getPollutantData.getCityPollutionEpisode(city);
            redisTemplate.opsForValue().set(key, pollutantCity);
        } else {
            //有缓存就取出来
            BeanUtils.copyProperties(javaObject, pollutantCity);
        }
        stringRedisTemplate.expire(key, 30, TimeUnit.MINUTES);
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        if (pollutantCity != null) {
            result.setMsg(true);
            result.setTotal(Long.valueOf(pollutantCity.getPollutionSiteList().size()));
        }
        result.setData(pollutantCity);
        return result;
    }


    //这里等到没有问题之后，换成pageInfo分页
    @ApiOperation(value = "获取全国PM2.5指数排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码,默认1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "页面大小,默认20", dataType = "int")
    })
    @RequestMapping(value = "/getNation", method = RequestMethod.POST)
    public ResponseResult getNation(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
                                    @RequestParam(value = "size", defaultValue = "20") int pageSize) {
        GetPollutantData getPollutantData = new GetPollutantData();
        List<PollutantCity> pollutantCityList = new ArrayList<>();
        List<PollutantCity> redisList = new ArrayList<>();
        //每次响应都要去redis看看有没有这个value可以去取
        String key = "NationPollutionSituation";
        Long length = redisTemplate.opsForList().size(key);
        for (Long i = 0L; i < length; i++) {
            JSON json = (JSON) JSON.toJSON(redisTemplate.opsForList().index(key, i));
            Object javaObject = JSON.toJavaObject(json, PollutantCity.class);
            PollutantCity result = new PollutantCity();
            BeanUtils.copyProperties(javaObject, result);
            redisList.add(result);
        }
        if (redisList.size() == 0) {
            //没有缓存就存进去
            pollutantCityList = getPollutantData.getNationPollutantRank();
            pollutantCityList.forEach(item -> {
                redisTemplate.opsForList().rightPush(key, item);
            });
        } else {
            //有缓存就取出来
            pollutantCityList.addAll(redisList);
        }
        stringRedisTemplate.expire(key, 30, TimeUnit.MINUTES);

        List<PollutantCity> pageList = new ArrayList<>();
        int size = pollutantCityList.size();
        int pageStart = pageIndex == 1 ? 0 : (pageIndex - 1) * pageSize;
        int pageEnd = size < pageIndex * pageSize ? size : pageIndex * pageSize;
        if (size>pageStart){
            pageList = pollutantCityList.subList(pageStart,pageEnd);
        }
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        if (pollutantCityList != null) {
            result.setMsg(true);
            result.setTotal(Long.valueOf(pollutantCityList.size()));
        }
        result.setData(pageList);
        return result;
    }
}
