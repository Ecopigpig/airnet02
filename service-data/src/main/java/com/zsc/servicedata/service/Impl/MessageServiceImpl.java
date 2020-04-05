package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.entity.alarm.Message;
import com.zsc.servicedata.mapper.MessageMapper;
import com.zsc.servicedata.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int insertNewMessage(Message message) {
        return messageMapper.insertNewMessage(message);
    }

    @Override
    public Message getMessageContext(Long messageId) {
        return messageMapper.getMessageContext(messageId);
    }

    @Override
    public int updateMessageRead(Long messageId) {
        return messageMapper.updateMessageRead(messageId);
    }

    @Override
    public int deleteMessage(List<Long> idList) {
        return messageMapper.deleteMessage(idList);
    }

    @Override
    public List<Message> selectMessageListByUser(Long userId) {
        return messageMapper.selectMessageListByUser(userId);
    }

    @Override
    public Map<String,Integer> selectCountInCondition(Long userId) {
        Map<String,Integer> map = new HashMap<>();
        int total = messageMapper.selectCountInCondition(userId,-1);
        int read = messageMapper.selectCountInCondition(userId,0);
        int unRead = messageMapper.selectCountInCondition(userId,1);
        map.put("total",total);
        map.put("read",read);
        map.put("unRead",unRead);
        return map;
    }

    @Override
    public int getTotal() {
        return messageMapper.selectTotal();
    }
}
