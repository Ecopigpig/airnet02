package com.zsc.servicedata.service;

import com.zsc.servicedata.entity.alarm.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {
    /**
     * 监测警报的站内信
     * @param message
     * @return
     */
    int insertNewMessage(Message message);

    /**
     * 读取站内信内容
     * @param messageId
     * @return
     */
    Message getMessageContext(Long messageId);

    /**
     * 用户已读站内信
     * @param messageId
     * @return
     */
    int updateMessageRead(Long messageId);

    /**
     * 用户删除站内信,批量
     * @param idList
     * @return
     */
    int deleteMessage(List<Long> idList);

    /**
     * 显示该用户的所有站内信
     * @param userId
     * @return
     */
    List<Message> selectMessageListByUser(Long userId);

    /**
     * 返回站内信各种数量问题
     * @param userId
     * @return
     */
    Map<String,Integer> selectCountInCondition(Long userId);

    int getTotal();
}
