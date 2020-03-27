package com.zsc.servicedata.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.service.UserService;
import com.zsc.servicedata.tag.MyLog;
import com.zsc.servicedata.tag.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.page.PageParam;
import model.result.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "UserController",tags = "用户控制器")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/registerAccount", method = RequestMethod.POST)
    public ResponseResult airQualityHistoryChart(@RequestBody UserInfo param) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        //用户名不可重复
        UserInfo user = new UserInfo();
        user.setUsername(param.getUsername());
        UserInfo userInfo = userService.confirmUser(user);
        if(userInfo!=null){
            result.setData("用户名唯一,该用户已存在!");
            return result;
        }else{
            if(param.getPassword()==null){
                result.setData("密码不能为空");
                return result;
            }
            if(param.getEmail()==null){
                result.setData("邮件不能为空,这会影响你新增污染监测点哦.");
                return result;
            } else {
                boolean matcher = param.getEmail().matches("[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}");
                if(!matcher){
                    result.setData("请输入正确的邮箱地址");
                    return result;
                }
                else{
                   int i = userService.addNewUser(param);
                   if(i>0){
                       result.setMsg(true);
                       result.setData("注册成功!");
                       return result;
                   }
                }
            }
        }
        return result;
    }

    @ApiOperation(value = "用户列表显示")
    @RequestMapping(value = "/listAllUsers", method = RequestMethod.POST)
    public ResponseResult listAllUsers(@RequestBody PageParam pageParam) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        int pageIndex = pageParam.getPage()<=0?1:pageParam.getPage();
        int pageSize = pageParam.getSize()<=0?20:pageParam.getSize();
        PageHelper.startPage(pageIndex,pageSize);
        List<UserInfo> userInfoList = userService.getAllUsers();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoList);
        if(pageInfo.getSize()>0){
            result.setMsg(true);
            result.setData(pageInfo);
        }
        return result;
    }

    @ApiOperation(value = "用户修改个人资料")
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    public ResponseResult listAllUsers(@RequestBody UserInfo userInfo) {
        ResponseResult result = new ResponseResult();
        result.setMsg(false);
        if(userInfo.getId()==null){
            result.setData("用户ID不能为空");
            return result;
        } else {
            String username = userInfo.getUsername();
            if(username!=null) {
                UserInfo nameUser = new UserInfo();
                nameUser.setUsername(username);
                UserInfo exitUser = userService.confirmUser(nameUser);
                if (exitUser != null) {
                    result.setData("用户名已存在,请换别的名字");
                    return result;
                }
            }else{
                UserInfo oldUser = userService.getUserById(userInfo.getId());
                userInfo.setUsername(oldUser.getUsername());
            }
            int i = userService.updateUserInfo(userInfo);
            if(i>0){
                result.setMsg(true);
                return result;
            }
        }
        return result;
    }

    /*假装登录，将用户信息存到session（方法是我之前写的懒得改，所以直接取的第一条数据）*/
    @RequestMapping("/login")
    public List<UserInfo> login(HttpServletRequest request){
        List<UserInfo> user = userService.getAllUsers();
        UserInfo user1 = user.get(0);
        request.getSession().setAttribute("user",user1);
        return user;
    }
    /*记录日志*/
    @UserLoginToken
    @MyLog(operation = "查询用户信息",type = 1)
    @RequestMapping("/log")
    public List<UserInfo> insertLog(HttpServletRequest request){
        List<UserInfo> user = userService.getAllUsers();
        return user;
    }
}
