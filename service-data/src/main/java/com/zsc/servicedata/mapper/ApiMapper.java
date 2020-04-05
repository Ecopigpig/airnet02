package com.zsc.servicedata.mapper;

import com.zsc.servicedata.entity.data.ApplyRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ApiMapper {
    int insertApplyRecord(ApplyRecord applyRecord);

    List<ApplyRecord> selectAllApiRecords();

    int updateAuthStatus(@Param("list") List<Long> userIdList,@Param("auth")Integer auth);

    List<ApplyRecord> selectRecordsByUser(@Param("userId") Long userId);
}
