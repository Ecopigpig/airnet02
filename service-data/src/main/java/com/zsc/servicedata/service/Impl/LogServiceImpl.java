package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.mapper.MyLogMapper;
import com.zsc.servicedata.service.LogService;
import model.log.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private MyLogMapper myLogMapper;

    @Override
    public List<Operation> getLogList() {
        return myLogMapper.selectAll();
    }

    @Override
    public int getLoginUserNum() {
        return myLogMapper.selectNumByCondition(0);
    }

    @Override
    public int getLogTotal() {
        return myLogMapper.selectNumByCondition(-1);
    }
}
