package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.alarm.Message;
import com.zsc.servicedata.entity.data.ApplyRecord;
import com.zsc.servicedata.entity.data.TokenResult;
import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.entity.param.ApiParam;
import com.zsc.servicedata.service.ApiService;
import com.zsc.servicedata.service.MessageService;
import com.zsc.servicedata.service.TokenService;
import com.zsc.servicedata.service.UserService;
import com.zsc.servicedata.tag.MyLog;
import com.zsc.servicedata.tag.UserLoginToken;
import com.zsc.servicedata.utils.token.TokenUtil;
import com.zsc.servicedata.utils.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.page.PageParam;
import model.result.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    MessageService messageService;

    @UserLoginToken
    @MyLog(operation = "用户获取其申请的API",type = 1)
    @ApiOperation(value = "用户获取其申请的API")
    @RequestMapping(value = "/getApi", method = RequestMethod.POST)
    public ResponseResult getApi(@RequestBody Map<String,Long> map) {
        ResponseResult result = new ResponseResult();
        Long userId = map.get("id");
        UserInfo userForBase = userService.getUserById(userId);
        result.setMsg(false);
        TokenResult tokenResult = new TokenResult();
        if(userForBase == null){
            //数据库中没有这个用户，就是说连申请用API的资格都没有
            String str = "该用户不存在基础账号,无法申请API";
            result.setData(str);
            return result;
        }else{
            //数据库中存在这个用户,那么把这个用户插入到申请API的表中
            //判断是否允许申请API
            if(userForBase.getAuth().equals(0)){
                tokenResult.setAuthStatus(0);
                tokenResult.setApiToken("您还没申请API呢");
                result.setData(tokenResult);
                return result;
            }
            else if(userForBase.getAuth().equals(1)){
                tokenResult.setAuthStatus(1);
                tokenResult.setApiToken("您申请的API还没通过审核呢");
                result.setData(tokenResult);
                return result;
            }
            else if(userForBase.getAuth().equals(3)){
                tokenResult.setAuthStatus(3);
                tokenResult.setApiToken("您申请的API被驳回了,请再次申请");
                result.setData(tokenResult);
                return result;
            }else{
                String token = userForBase.getToken();
                if(token!=null){
                    result.setMsg(true);
                    tokenResult.setAuthStatus(2);
                    tokenResult.setApiToken(token);
                    result.setData(tokenResult);
                    return result;
                }else{
                    //生成API
                    token = tokenService.getAPIToken(userForBase);
                    //把它存到用户API数据表中
                    int i =userService.insertToken(userForBase.getId(),token);
                    if(i>0){
                        tokenResult.setAuthStatus(2);
                        tokenResult.setApiToken(token);
                        result.setData(tokenResult);
                    }
                }
                return result;
            }
        }
    }

    @UserLoginToken
    @MyLog(operation = "用户提交API调用申请",type = 2)
    @ApiOperation(value = "用户提交API调用申请")
    @RequestMapping(value = "/applyApi", method = RequestMethod.POST)
    public ResponseResult getApi(@RequestBody ApplyRecord applyRecord) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        applyRecord.setApplyTime(new Date());
        int i = apiService.addNewApplyRecord(applyRecord);
        String userId = TokenUtil.getTokenUserId();
        UserInfo userForBase = userService.getUserById(Long.valueOf(userId));
        if(userForBase.getAuth().equals(1)){
            result.setMsg(true);
            result.setData("您已提交申请,请耐心等待审核");
        }else{
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(Long.valueOf(userId));
            apiService.auditApi(userIdList,1);
            if(i>0){
                result.setMsg(true);
                result.setData("申请成功");
            }
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "后台查看用户API申请列表",type = 1)
    @ApiOperation(value = "后台查看用户API申请列表")
    @RequestMapping(value = "/apiList", method = RequestMethod.POST)
    public ResponseResult apiList(@RequestBody ApiParam apiParam) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        PageHelper.startPage(apiParam.getPage(),apiParam.getSize());
        List<ApplyRecord> list = apiService.getAllApiRecords(apiParam.getAuth());
        if(list.size()>0){
            result.setMsg(true);
            PageInfo<ApplyRecord> pageInfo = new PageInfo<>(list);
            result.setData(pageInfo);
            result.setTotal(Long.valueOf(pageInfo.getSize()));
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "管理员批量审批通过用户的API申请",type = 2)
    @ApiOperation(value = "管理员批量审批通过用户的API申请")
    @RequestMapping(value = "/auditApi", method = RequestMethod.POST)
    public ResponseResult auditApi(@RequestBody Map<String,List<Long>> map) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<Long> userIdList = map.get("list");
        //更改用户申请的状态
        int i = apiService.auditApi(userIdList,2);
        Map<Long,UserInfo> userInfoMap = userService.getUserByIdList(userIdList);
        if(i>0){
            //生成用户唯一的token
            result.setMsg(true);
            userInfoMap.forEach((id,userInfo)->{
                String token = tokenService.getAPIToken(userInfo);
                //把它存到用户API数据表中
                userService.insertToken(id,token);
                Message message = new Message();
                message.setSendTime(new Date());
                String context = "您的API申请已获审批,您的API-KEY为："+"\n"+token;
                message.setContext(context);
                message.setIsRead((byte)0);
                message.setUserId(userInfo.getId());
                int j = messageService.insertNewMessage(message);
                if(j>0) {
                    try {
                        WebSocketServer.sendAlarmMessageInSide(context, String.valueOf(userInfo.getId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            result.setData("全部审核通过！");
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "管理员批量驳回用户的API申请",type = 2)
    @ApiOperation(value = "管理员批量驳回用户的API申请")
    @RequestMapping(value = "/rejectApi", method = RequestMethod.POST)
    public ResponseResult rejectApi(@RequestBody Map<String,List<Long>> map) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<Long> userIdList = map.get("list");
        //更改用户申请的状态
        int i = apiService.auditApi(userIdList,3);
        if(i>0){
            result.setMsg(true);
            result.setData("完成全部驳回！");
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "用户查看自己的API申请记录",type = 1)
    @ApiOperation(value = "用户查看自己的API申请记录")
    @RequestMapping(value = "/checkApplyRecordByUser",method = RequestMethod.POST)
    public ResponseResult checkApplyRecordByUser(@RequestBody Map<String,Long> map){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        Long userId = map.get("id");
        List<ApplyRecord> applyRecordList = apiService.getRecordsByUser(userId);
        if(applyRecordList.size()>0){
            result.setMsg(true);
            result.setData(applyRecordList);
            result.setTotal(Long.valueOf(applyRecordList.size()));
        }
        return result;
    }

    @RequestMapping("/getWaitAuditNum")
    public ResponseResult getWaitAuditNum(){
        ResponseResult result = new ResponseResult();
        result.setMsg(true);
        int num = apiService.getWaitAuditNum();
        result.setData(num);
        return result;
    }
}
