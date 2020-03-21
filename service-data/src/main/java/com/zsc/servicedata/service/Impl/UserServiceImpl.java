package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.entity.data.UserInfo;
import com.zsc.servicedata.mapper.UserMapper;
import com.zsc.servicedata.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public Map<Long,String> getAllUserEmail(Set<Long> userIdList) {
        Map<Long, UserInfo> map;
        map =  userMapper.selectAllUserEmail(userIdList);
        Map<Long,String> resultMap = new HashMap<>();
        for(Long id :map.keySet()){
            UserInfo userInfo = map.get(id);
            resultMap.put(userInfo.getId(),userInfo.getEmail());
        }
        return resultMap;
    }

    @Override
    public UserInfo getUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public UserInfo confirmUser(UserInfo userInfo) {
        return userMapper.confirmUser(userInfo);
    }

    @Override
    public int insertToken(Long userId, String token) {
        return userMapper.updateToken(userId,token);
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public int addNewUser(UserInfo param) {
        return userMapper.insertNewUser(param);
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) {
        return userMapper.updateUserInfo(userInfo);
    }
}
