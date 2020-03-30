package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.entity.param.AqiHistoryParam;
import com.zsc.servicedata.entity.param.DetailCityParam;
import com.zsc.servicedata.service.AirService;
import com.zsc.servicedata.service.CityService;
import com.zsc.servicedata.service.feign.HiFeignService;
import com.zsc.servicedata.tag.MyLog;
import com.zsc.servicedata.tag.PassToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.air.HistoryAqiChart;
import model.page.PageParam;
import model.result.ResponseResult;
import model.weather.AreaCode;
import model.weather.CityCode;
import model.weather.InstanceWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "CityController",tags = "城市相关控制器")
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    @Resource
    private AirService airService;


    @Autowired
    private HiFeignService hiFeignService;

    @PassToken
    @ApiOperation(value = "通过城市名称获取其对应的实时天气areaCode")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "所在市名,不必带市字", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "area", value = "市内城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAreaCode", method = RequestMethod.POST)
    public ResponseResult getAreaCode(@RequestBody DetailCityParam param) {
        String city = param.getCity();
        String area = param.getArea();
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
    @RequestMapping(value = "/getActualTimeWeather",method = RequestMethod.POST)
    public ResponseResult getActualTimeWeather(@RequestBody DetailCityParam param) {
        String city = param.getCity();
        String area = param.getArea();
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        //先去获取城市的代码
        AreaCode areaCode = cityService.selectCodeByAreaName(city,area);
        //没有,去调API查一次再存到数据库里头
        if(areaCode==null) {
            List<AreaCode> areaCodeList = hiFeignService.getAreaCode(city);
            int i = cityService.insertAreaCode(areaCodeList);
            //如果这里再次出现两个相同记录的bug情况,就把返回值改成list再去取第一个即可。
            if(i>0) {
                areaCode = cityService.selectCodeByAreaName(city,area);
            }
        }
        //在去用代码查询实时天气
        CityCode cityCode = new CityCode();
        cityCode.setAreaCode(areaCode.getAreaCode());
        cityCode.setPostalCode(areaCode.getPostalCode());
        InstanceWeather instanceWeather = hiFeignService.getInstanceWeather(cityCode);
        if(instanceWeather!=null){
            instanceWeather.setCity(city+area);
            result.setMsg(true);
            result.setData(instanceWeather);
        }
        return result;
    }

    @ApiOperation(value = "按条件获取空气质量历史排行榜")
    @RequestMapping(value = "/airQualityHistoryChart", method = RequestMethod.POST)
    public ResponseResult airQualityHistoryChart(@RequestBody AqiHistoryParam param) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        //参数校验
        if(param.getRecordSize()==null||param.getRecordSize()<=0) param.setRecordSize(10L);
        if(param.getOrder()==null||param.getOrder().equals("")){
            param.setOrder("asc");
        }else{
            String order = param.getOrder().toUpperCase();
            if(order.equals("ASC")) param.setOrder("ASC");
            else if(order.equals("DESC")) param.setOrder("DESC");
            else param.setOrder("ASC");
        }
        int pageIndex = param.getPage()<=0?1:param.getPage();
        PageHelper.startPage(pageIndex, Math.toIntExact(param.getRecordSize()));
        List<HistoryAqiChart> historyAqiChartList = airService.getAqiHistoryByCondition(param);
        PageInfo<HistoryAqiChart> pageInfo = new PageInfo<>(historyAqiChartList);
        if(historyAqiChartList.size()>0){
            result.setMsg(true);
            result.setData(pageInfo);
            result.setTotal(pageInfo.getTotal());
        }
        return result;
    }
}
