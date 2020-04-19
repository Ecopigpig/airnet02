package com.zsc.servicedata.controller;

import com.zsc.servicedata.entity.data.Pollutant;
import com.zsc.servicedata.entity.param.PollutionMonitorParam;
import com.zsc.servicedata.entity.result.ResponseResult;
import com.zsc.servicedata.service.PollutionService;
import com.zsc.servicedata.service.UserService;
import com.zsc.servicedata.tag.MyLog;
import com.zsc.servicedata.tag.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import model.pollutant.MonitorSite;
import model.pollutant.PollutionMonitorResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "MonitorController", tags = "监测控制器")
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private UserService userService;

    @Resource
    private PollutionService pollutionService;

    @UserLoginToken
    @MyLog(operation = "根据用户ID获取用户的监测情况列表", type = 1)
    @ApiOperation(value = "根据用户ID获取用户的监测情况列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/getMonitorListByUser", method = RequestMethod.POST)
    public ResponseResult getMonitorListByUser(@RequestBody Map<String, Long> map) {
        Long userId = map.get("id");
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<Pollutant> pollutantList = pollutionService.getMonitorListByUser(userId);
        if (pollutantList.size() > 0) {
            result.setMsg(true);
            result.setData(pollutantList);
            result.setTotal(Long.valueOf(pollutantList.size()));
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "用户自行添加监测点", type = 2)
    @ApiOperation(value = "用户自行添加监测点")
    @RequestMapping(value = "/setMonitor", method = RequestMethod.POST)
    public ResponseResult setMonitor(@RequestBody List<PollutionMonitorParam> paramList) {
        ResponseResult result = new ResponseResult();
        List<PollutionMonitorResult> resultList = new ArrayList<>();
        result.setMsg(false);
        try {
            paramList.forEach(monitor -> {
                PollutionMonitorResult monitorResult = new PollutionMonitorResult();
                BeanUtils.copyProperties(monitor, monitorResult);
                monitorResult.setQuality(changeIntoQualityStr(monitor.getQuality()));
                resultList.add(monitorResult);
            });
            pollutionService.setMonitor(resultList);
            result.setMsg(true);
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        return result;
    }

    private static String changeIntoQualityStr(int quality) {
        String qualityStr = "";
        switch (quality) {
            case 1:
                qualityStr = "优质";
                break;
            case 2:
                qualityStr = "良好";
                break;
            case 3:
                qualityStr = "轻度污染";
                break;
            case 4:
                qualityStr = "中度污染";
                break;
            case 5:
                qualityStr = "重度污染";
                break;
            case 6:
                qualityStr = "严重污染";
                break;
            default:
                break;
        }
        return qualityStr;
    }


    @ApiOperation(value = "通过城市名称获取其下的监测点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "city", value = "城市名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getMonitorPointInCity", method = RequestMethod.POST)
    public ResponseResult getMonitorPointInCity(@RequestBody Map<String, String> map) throws Exception {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        String city = map.get("city");
        List<MonitorSite> monitorSiteList = pollutionService.getMonitorPointInCity(city);
        if (monitorSiteList.size() > 0) {
            result.setMsg(true);
            result.setData(monitorSiteList);
        }
        return result;
    }


    @UserLoginToken
    @MyLog(operation = "用户编辑监测点", type = 2)
    @ApiOperation(value = "用户编辑监测点")
    @RequestMapping(value = "/editMonitor", method = RequestMethod.POST)
    public ResponseResult editMonitor(@RequestBody PollutionMonitorParam monitorParam) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        try {
            PollutionMonitorResult monitorResult = new PollutionMonitorResult();
            BeanUtils.copyProperties(monitorParam, monitorResult);
            monitorResult.setQuality(changeIntoQualityStr(monitorParam.getQuality()));
            int n = pollutionService.editMonitor(monitorResult);
            if (n > 0) {
                result.setMsg(true);
                return result;
            }
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "用户删除监测点", type = 2)
    @ApiOperation(value = "用户删除监测点")
    @RequestMapping(value = "/deleteMonitor", method = RequestMethod.POST)
    public ResponseResult deleteMonitor(@RequestParam("id") Long id) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        try {
            pollutionService.deleteMonitor(id);
            result.setMsg(true);
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        return result;
    }
}
