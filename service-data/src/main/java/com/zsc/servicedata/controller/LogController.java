package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.result.ResponseResult;
import com.zsc.servicedata.service.LogService;
import io.swagger.annotations.Api;
import model.log.Operation;
import model.page.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "LogController", tags = "日志控制器")
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping("/getLogList")
    public ResponseResult getLogList(@RequestBody PageParam pageParam){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        PageHelper.startPage(pageParam.getPage(),pageParam.getSize());
        List<Operation> operationList = logService.getLogList();
        PageInfo<Operation> pageInfo = new PageInfo<>(operationList);
        if(operationList.size()>0){
            result.setMsg(true);
            result.setData(pageInfo);
        }
        return result;
    }

    @RequestMapping("/getLoginUserNum")
    public ResponseResult getLoginUserNum(){
        ResponseResult result = new ResponseResult();
        result.setMsg(true);
        int num = logService.getLoginUserNum();
        result.setData(num);
        return result;
    }

    @RequestMapping("/getLogTotal")
    public ResponseResult getLogTotal(){
        ResponseResult result = new ResponseResult();
        result.setMsg(true);
        int num = logService.getLogTotal();
        result.setData(num);
        return result;
    }
}
