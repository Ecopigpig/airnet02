package com.zsc.servicedata.controller;

import com.alibaba.fastjson.JSONObject;
import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.mapper.MyLogMapper;
import com.zsc.servicedata.service.TokenService;
import com.zsc.servicedata.service.UserService;
import com.zsc.servicedata.tag.MyLog;
import com.zsc.servicedata.tag.UserLoginToken;
import com.zsc.servicedata.utils.token.TokenUtil;
import com.zsc.servicedata.utils.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.log.Operation;
import model.result.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Api(value = "LoginController", tags = "登录控制器")
@RestController
@RequestMapping("/check")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MyLogMapper myLogMapper;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult login(HttpServletRequest request, @RequestBody UserInfo user) {
        ResponseResult result = new ResponseResult();
        JSONObject jsonObject = new JSONObject();
        UserInfo userForBase = userService.confirmUser(user);
        result.setMsg(false);
         if (userForBase == null) {
            jsonObject.put("message", "登录失败,用户不存在");
            result.setData(jsonObject);
            return result;
        } else {
            if (!userForBase.getPassword().equals(user.getPassword())) {
                jsonObject.put("message", "登录失败,密码错误");
                result.setData(jsonObject);
                return result;
            } else {
                String token = (String) redisTemplate.opsForValue().get(userForBase.getId() + "token");
                if (token == null) {
                    token = tokenService.getToken(userForBase);
                    redisTemplate.opsForValue().set(userForBase.getId() + "token", token);
                    stringRedisTemplate.expire(userForBase.getId() + "token", 30, TimeUnit.MINUTES);
                }
                jsonObject.put("token", token);
                jsonObject.put("user",userForBase);
                Operation adminLog = new Operation();
                adminLog.setUserId(userForBase.getId());
                adminLog.setUserName(userForBase.getUsername());
                adminLog.setLoginIp(request.getRemoteAddr());
                adminLog.setType(0);
                adminLog.setUrl(request.getRequestURL().toString());
                adminLog.setOperation("用户登录");
                adminLog.setCreateTime(new Date());
                myLogMapper.insertLog(adminLog);
                result.setMsg(true);
                result.setData(jsonObject);
                return result;
            }
        }
    }

    @ApiOperation(value = "用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseResult logout(@RequestBody UserInfo user) {
        JSONObject jsonObject = new JSONObject();
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        UserInfo userForBase = userService.confirmUser(user);
        if (userForBase == null) {
            jsonObject.put("message", "登出用户信息不正确");
            result.setData(jsonObject);
            return result;
        } else {
            redisTemplate.delete(userForBase.getId()+"token");
            result.setMsg(true);
            return result;
        }
    }


    /***
     * 这个请求需要验证token才能访问
     *
     * @author: MRC
     * @date 2019年5月27日 下午5:45:19
     * @return String 返回类型
     */
    @RequestMapping(value = "/sendWebSocket",method = RequestMethod.POST)
    public String getMessage(@RequestParam("userId")String userId) throws IOException {
        // 取出token中带的用户id 进行操作
        WebSocketServer.sendAlarmMessageInSide("websocket后端给前端发消息",userId);
        return "websocket信息已发送";
    }
}
