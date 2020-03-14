package com.zsc.servicedata.service;

import com.zsc.servicedata.entity.data.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    String getToken(UserInfo userInfo);

    String getAPIToken(UserInfo userInfo);

}
