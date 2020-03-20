package com.zsc.servicedata.controller;

import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.service.TokenService;
import com.zsc.servicedata.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.result.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "ApiController",tags = "API控制器")
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "用户获取其申请的API")
    @RequestMapping(value = "/getApi", method = RequestMethod.POST)
    public ResponseResult getApi(@RequestBody UserInfo userInfo) {
        Long userId = userInfo.getId();
        String password = userInfo.getPassword();
        ResponseResult result = new ResponseResult();
        UserInfo user = new UserInfo();
        user.setId(userId);
        user.setPassword(password);
        UserInfo userForBase = userService.confirmUser(user);
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
                //生成API
                String token = tokenService.getAPIToken(userForBase);
                //把它存到用户API数据表中
                int i =userService.insertToken(userForBase.getId(),token);
                if(i>0){
                    result.setMsg(true);
                    result.setData(token);
                }
                return result;
            }
        }
    }
}
