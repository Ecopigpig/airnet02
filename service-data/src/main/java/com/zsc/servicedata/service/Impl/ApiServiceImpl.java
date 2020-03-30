package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.entity.data.ApplyRecord;
import com.zsc.servicedata.mapper.ApiMapper;
import com.zsc.servicedata.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("apiService")
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ApiMapper apiMapper;

    @Override
    public int addNewApplyRecord(ApplyRecord applyRecord) {
        return apiMapper.insertApplyRecord(applyRecord);
    }

    @Override
    public List<ApplyRecord> getAllApiRecords() {
        return apiMapper.selectAllApiRecords();
    }

    @Override
    public int auditApi(List<Long> userIdList) {
        return apiMapper.updateAuthStatus(userIdList);
    }
}
