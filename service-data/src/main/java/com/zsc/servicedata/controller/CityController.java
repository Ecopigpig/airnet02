package com.zsc.servicedata.controller;

import com.zsc.servicedata.entity.param.AqiHistoryParam;
import com.zsc.servicedata.service.AirService;
import com.zsc.servicedata.service.CityService;
import com.zsc.servicedata.service.PollutionService;
import com.zsc.servicedata.service.feign.HiFeignService;
import com.zsc.servicedata.tag.PassToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.air.HistoryAqiChart;
import model.result.ResponseResult;
import model.weather.AreaCode;
import model.weather.InstanceWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "CityController",tags = "城市相关控制器")
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CityService cityService;

    @Resource
    private AirService airService;

    @Autowired
    private PollutionService pollutionService;

    @Autowired
    private HiFeignService hiFeignService;

    @PassToken
    @ApiOperation(value = "通过城市名称获取其对应的实时天气areaCode")
    @RequestMapping(value = "/getAreaCode", method = RequestMethod.GET)
    public ResponseResult getAreaCode(@RequestParam("city") String city,
                                      @RequestParam("area")String area) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        //先去数据库里面查询,是否有这个城市的areaCode
        //有,直接返回
        AreaCode areaCode = cityService.selectCodeByAreaName(city,area);
        //没有,去调API查一次再存到数据库里头
        if(areaCode==null) {
            List<AreaCode> areaCodeList = hiFeignService.getAreaCode(city);
            int i = cityService.insertAreaCode(areaCodeList);
            if(i>0) {
                areaCode = cityService.selectCodeByAreaName(city,area);
            }
            result.setMsg(true);
            result.setData(areaCode);
        }
        else {
            result.setMsg(true);
            result.setData(areaCode);
        }
        return result;
    }



    //城市的实时天气情况
    @ApiOperation(value = "根据城市中文名获取该城市的实时天气情况")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "所在市名,不必带市字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "area", value = "市内城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getActualTimeWeather",method = RequestMethod.GET)
    public ResponseResult getActualTimeWeather(@RequestParam String city,@RequestParam String area) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        //先去获取城市的代码
        AreaCode areaCode = cityService.selectCodeByAreaName(city,area);
        //没有,去调API查一次再存到数据库里头
        if(areaCode==null) {
            List<AreaCode> areaCodeList = hiFeignService.getAreaCode(city);
            int i = cityService.insertAreaCode(areaCodeList);
            if(i>0) {
                areaCode = cityService.selectCodeByAreaName(city,area);
            }
        }
        //在去用代码查询实时天气
        InstanceWeather instanceWeather = hiFeignService.getInstanceWeather(areaCode.getAreaCode(),areaCode.getPostalCode());
        if(instanceWeather!=null){
            instanceWeather.setCity(city+area);
            result.setMsg(true);
            result.setData(instanceWeather);
        }
        return result;
    }

    @ApiOperation(value = "按条件获取空气质量历史排行榜")
    @RequestMapping(value = "/airQualityHistoryChart", method = RequestMethod.GET)
    public ResponseResult airQualityHistoryChart(@RequestBody AqiHistoryParam param) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        //参数校验
        if(param.getSize()==null||param.getSize()<=0) param.setSize(10L);
        if(param.getOrder()==null||param.getOrder().equals("")){
            param.setOrder("asc");
        }else{
            String order = param.getOrder().toUpperCase();
            if(order.equals("ASC")) param.setOrder("ASC");
            else if(order.equals("DESC")) param.setOrder("DESC");
            else param.setOrder("ASC");
        }
        List<HistoryAqiChart> historyAqiChartList = airService.getAqiHistoryByCondition(param);
        if(historyAqiChartList.size()>0){
            result.setMsg(true);
            result.setData(historyAqiChartList);
            result.setTotal(Long.valueOf(historyAqiChartList.size()));
        }
        return result;
    }
}
