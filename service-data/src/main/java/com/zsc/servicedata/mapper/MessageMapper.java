package com.zsc.servicedata.mapper;

import com.zsc.servicedata.entity.alarm.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageMapper {

    int insertNewMessage(Message message);

    List<Message> selectMessageListByUser(Long userId);

    int updateMessageRead(Long messageId);

    int deleteMessage(@Param("list") List<Long> idList);

    int selectCountInCondition(@Param("userId") Long userId,@Param("status") Integer status);

    Message getMessageContext(Long messageId);
}
