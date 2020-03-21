package com.zsc.servicedata.mapper;

import com.zsc.servicedata.entity.data.UserInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface UserMapper {
    /**
     * 通过用户ID批量查询用户邮箱
     * @param userIdList
     * @return
     */
    @MapKey("id")
    Map<Long, UserInfo> selectAllUserEmail(@Param("list") Set<Long> userIdList);

    UserInfo selectUserById(@Param("id") Long id);

    UserInfo confirmUser(UserInfo userInfo);

    int updateToken(Long userId, String token);

    List<UserInfo> selectAllUsers();

    int insertNewUser(UserInfo param);

    int updateUserInfo(UserInfo userInfo);
}
