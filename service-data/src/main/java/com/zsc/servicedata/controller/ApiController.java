package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.data.ApplyRecord;
import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.service.ApiService;
import com.zsc.servicedata.service.TokenService;
import com.zsc.servicedata.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.page.PageParam;
import model.result.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "ApiController",tags = "API控制器")
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    UserService userService;

    @Autowired
    ApiService apiService;

    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "用户获取其申请的API")
    @RequestMapping(value = "/getApi", method = RequestMethod.POST)
    public ResponseResult getApi(@RequestBody Map<String,Long> map) {
        ResponseResult result = new ResponseResult();
        Long userId = map.get("id");
        UserInfo userForBase = userService.getUserById(userId);
        result.setMsg(false);
        if(userForBase == null){
            //数据库中没有这个用户，就是说连申请用API的资格都没有
            String str = "该用户不存在基础账号,无法申请API";
            result.setData(str);
            return result;
        }else{
            //数据库中存在这个用户,那么把这个用户插入到申请API的表中
            //判断是否允许申请API
            if(userForBase.getAuth().equals(0)){
                result.setData("请先去申请API,审核通过后即可获得API");
                return result;
            }else{
                String token = userForBase.getToken();
                if(token!=null){
                    result.setMsg(true);
                    result.setData(token);
                    return result;
                }else{
                    //生成API
                    token = tokenService.getAPIToken(userForBase);
                    //把它存到用户API数据表中
                    int i =userService.insertToken(userForBase.getId(),token);
                    if(i>0){
                        result.setMsg(true);
                        result.setData(token);
                    }
                }
                return result;
            }
        }
    }

    @ApiOperation(value = "用户提交API调用申请")
    @RequestMapping(value = "/applyApi", method = RequestMethod.POST)
    public ResponseResult getApi(@RequestBody ApplyRecord applyRecord) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        applyRecord.setApplyTime(new Date());
        int i = apiService.addNewApplyRecord(applyRecord);
        if(i>0){
            result.setMsg(true);
            result.setData("申请成功");
        }
        return result;
    }

    @ApiOperation(value = "后台查看用户API申请列表")
    @RequestMapping(value = "/apiList", method = RequestMethod.POST)
    public ResponseResult apiList(@RequestBody PageParam pageParam) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        PageHelper.startPage(pageParam.getPage(),pageParam.getSize());
        List<ApplyRecord> list = apiService.getAllApiRecords();
        if(list.size()>0){
            result.setMsg(true);
            PageInfo<ApplyRecord> pageInfo = new PageInfo<>(list);
            result.setData(pageInfo);
            result.setTotal(Long.valueOf(pageInfo.getSize()));
        }
        return result;
    }

    @ApiOperation(value = "管理员审批通过用户的API申请")
    @RequestMapping(value = "/auditApi", method = RequestMethod.POST)
    public ResponseResult auditApi(@RequestBody Map<String,List<Long>> map) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<Long> userIdList = map.get("list");
        //更改用户申请的状态
        int i = apiService.auditApi(userIdList);
        Map<Long,UserInfo> userInfoMap = userService.getUserByIdList(userIdList);
        if(i>0){
            //生成用户唯一的token
            result.setMsg(true);
            userInfoMap.forEach((id,userInfo)->{
                String token = tokenService.getAPIToken(userInfo);
                //把它存到用户API数据表中
                userService.insertToken(id,token);
            });
            result.setData("全部审核通过！");
        }
        return result;
    }

}
