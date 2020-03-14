package com.zsc.servicedata.controller;

import com.zsc.servicedata.entity.data.Pollutant;
import com.zsc.servicedata.entity.param.PollutionMonitorParam;
import com.zsc.servicedata.entity.result.ResponseResult;
import com.zsc.servicedata.service.PollutionService;
import com.zsc.servicedata.service.UserService;
import com.zsc.servicedata.tag.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.pollutant.MonitorSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "MonitorController",tags = "监测控制器")
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private UserService userService;

    @Resource
    private PollutionService pollutionService;

    @UserLoginToken
    @ApiOperation(value = "根据用户ID获取用户的监测情况列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/getMonitorListByUser",method = RequestMethod.GET)
    public ResponseResult getMonitorListByUser(@RequestParam Long userId){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<Pollutant> pollutantList = pollutionService.getMonitorListByUser(userId);
        if(pollutantList.size()>0){
            result.setMsg(true);
            result.setData(pollutantList);
            result.setTotal(Long.valueOf(pollutantList.size()));
        }
        return result;
    }

    @UserLoginToken
    @ApiOperation(value = "用户自行添加监测点")
    @RequestMapping(value = "/setMonitor",method = RequestMethod.POST)
    public ResponseResult setMonitor(@RequestBody List<PollutionMonitorParam> paramList){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        try {
            pollutionService.setMonitor(paramList);
            result.setMsg(true);
        }catch (Exception e){
            result.setData(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "通过城市名称获取其下的监测点列表" )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMonitorPointInCity",method = RequestMethod.GET)
    public ResponseResult getMonitorPointInCity(@RequestParam String city) throws Exception {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<MonitorSite> monitorSiteList = pollutionService.getMonitorPointInCity(city);
        if(monitorSiteList.size()>0){
            result.setMsg(true);
            result.setData(monitorSiteList);
        }
        return result;
    }

}
