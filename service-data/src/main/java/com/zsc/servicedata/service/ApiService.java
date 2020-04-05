package com.zsc.servicedata.service;

import com.zsc.servicedata.entity.data.ApplyRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiService {
    int addNewApplyRecord(ApplyRecord applyRecord);

    List<ApplyRecord> getAllApiRecords();

    int auditApi(List<Long> userIdList,Integer auth);

    List<ApplyRecord> getRecordsByUser(Long userId);
}
