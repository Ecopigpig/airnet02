package com.zsc.servicedata.service;

import com.zsc.servicedata.entity.data.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface UserService {
    /**
     * 根据用户ID批量获取用户邮箱地址
     * @param userIdList
     * @return
     */
    Map<Long,String> getAllUserEmail(Set<Long> userIdList);

    /**
     * 根据用户id获取用户实体
     * @param userId
     * @return
     */
    UserInfo getUserById(Long userId);

    /**
     * 根据用户的登录信息确认是否存在该用户
     * @param userInfo
     * @return
     */
    UserInfo confirmUser(UserInfo userInfo);

    /**
     * 把用户申请通过的Token记录在数据库中
     * @param userId
     * @param token
     * @return
     */
    int insertToken(Long userId, String token);

    List<UserInfo> getAllUsers();
}
