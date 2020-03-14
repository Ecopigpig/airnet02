package com.zsc.servicedata.service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.service.TokenService;
import com.zsc.servicedata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UserService userService;

    @Override
    public String getToken(UserInfo userInfo) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        String token;

        token = JWT.create().withAudience(userInfo.getId().toString()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(userInfo.getPassword()));
        return token;
    }

    @Override
    public String getAPIToken(UserInfo userInfo) {
        String token;
        Date date = new Date();
        token = JWT.create().withAudience(userInfo.getId().toString()+date.toString()).sign(Algorithm.HMAC256(userInfo.getPassword()));
        return token;
    }
}
