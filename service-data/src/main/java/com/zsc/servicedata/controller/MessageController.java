package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.alarm.Message;
import com.zsc.servicedata.entity.result.ResponseResult;
import com.zsc.servicedata.service.MessageService;
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

    @ApiOperation(value = "获取该用户的站内信列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码,默认1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "页面大小,默认20", dataType = "int")
    })
    @RequestMapping(value = "/getUserMessageList",method = RequestMethod.POST)
    public ResponseResult getUserMessageList(@RequestParam(value = "page", defaultValue = "1") int pageIndex,
                                             @RequestParam(value = "size", defaultValue = "20") int pageSize,
                                             @RequestParam("userId")Long userId){
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

    @ApiOperation(value = "通过id获取站内信内容")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "站内信ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/getMessageContext",method = RequestMethod.POST)
    public ResponseResult getMessageContext(@RequestParam("messageId")Long messageId){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        Message message = messageService.getMessageContext(messageId);
        if(message!=null){
            result.setMsg(true);
            result.setData(message);
        }
        return result;
    }

    @ApiOperation(value = "把站内信改为已读状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "站内信ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/updateMessageRead",method = RequestMethod.POST)
    public ResponseResult updateMessageRead(@RequestParam("messageId")Long messageId){
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        int i = messageService.updateMessageRead(messageId);
        if(i>0){
            result.setMsg(true);
            result.setData("该站内信状态变成已读");
        }
        return result;
    }

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

    @ApiOperation(value = "获取用户站内信的各种状态数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "messageId", value = "站内信ID", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/selectCountInCondition",method = RequestMethod.POST)
    public ResponseResult selectCountInCondition(@RequestParam("userId")Long userId){
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
}
