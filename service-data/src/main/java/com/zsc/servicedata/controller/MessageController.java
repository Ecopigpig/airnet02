package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.alarm.Message;
import com.zsc.servicedata.entity.param.MessagePageParam;
import com.zsc.servicedata.entity.result.ResponseResult;
import com.zsc.servicedata.service.MessageService;
import com.zsc.servicedata.tag.MyLog;
import com.zsc.servicedata.tag.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "MessageController",tags = "站内信控制器")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @UserLoginToken
    @MyLog(operation = "获取该用户的站内信列表",type = 1)
    @ApiOperation(value = "获取该用户的站内信列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码,默认1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "页面大小,默认20", dataType = "int")
    })
    @RequestMapping(value = "/getUserMessageList",method = RequestMethod.POST)
    public ResponseResult getUserMessageList(@RequestBody MessagePageParam param){
        int pageIndex = param.getPage();
        int pageSize = param.getSize();
        Long userId = param.getUserId();
        ResponseResult result = new ResponseResult();
        result.setMsg(true);
        PageHelper.startPage(pageIndex,pageSize);
        List<Message> messageList = messageService.selectMessageListByUser(userId);
        PageInfo<Message> page = new PageInfo<>(messageList);
        if(messageList.size()>0){
            result.setData(page);
            result.setTotal(page.getTotal());
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "通过id获取站内信内容",type = 1)
    @ApiOperation(value = "通过id获取站内信内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "站内信ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/getMessageContext",method = RequestMethod.POST)
    public ResponseResult getMessageContext(@RequestBody Map<String,Long> map){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        Long messageId = map.get("messageId");
        Message message = messageService.getMessageContext(messageId);
        messageService.updateMessageRead(messageId);
        if(message!=null){
            result.setMsg(true);
            result.setData(message);
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "把站内信改为已读状态",type = 2)
    @ApiOperation(value = "把站内信改为已读状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "站内信ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/updateMessageRead",method = RequestMethod.POST)
    public ResponseResult updateMessageRead(@RequestBody Map<String,Long> map){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        Long messageId = map.get("messageId");
        int i = messageService.updateMessageRead(messageId);
        if(i>0){
            result.setMsg(true);
            result.setData("该站内信状态变成已读");
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "用户批量删除自己的站内信",type = 2)
    @ApiOperation(value = "用户批量删除自己的站内信")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageIdList", value = "站内信ID", required = true, dataType = "List")
    })
    @RequestMapping(value = "/deleteMessage",method = RequestMethod.POST)
    public ResponseResult deleteMessage(@RequestBody Map<String,List> map){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        List<Long> messageIdList = map.get("list");
        int i = messageService.deleteMessage(messageIdList);
        if(i>0){
            result.setMsg(true);
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "获取用户站内信的各种状态数量",type = 1)
    @ApiOperation(value = "获取用户站内信的各种状态数量")
    @RequestMapping(value = "/selectCountInCondition",method = RequestMethod.POST)
    public ResponseResult selectCountInCondition(@RequestBody Map<String,Long> paramMap){
        Long userId = paramMap.get("userId");
        ResponseResult result = new ResponseResult();
        result.setMsg(true);
        Map<String,Integer> map = messageService.selectCountInCondition(userId);
        if(map.size()==0){
            result.setData("很抱歉,当前您的站内信为空.");
        }else{
            result.setData(map);
        }
        return result;
    }

    @UserLoginToken
    @MyLog(operation = "获取数据库中站内信总数",type = 1)
    @ApiOperation(value = "获取数据库中站内信总数")
    @RequestMapping(value = "/getMessageCount",method = RequestMethod.POST)
    public ResponseResult getMessageCount(){
        ResponseResult result = new ResponseResult();
        result.setMsg(true);
        int total = messageService.getTotal();
        result.setData(total);
        return result;
    }

}
