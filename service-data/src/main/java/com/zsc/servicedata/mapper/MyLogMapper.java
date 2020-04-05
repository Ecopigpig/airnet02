package com.zsc.servicedata.mapper;

import model.log.Operation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MyLogMapper {

    void insertLog(Operation adminLog);

    List<Operation> selectAll();

    int selectNumByCondition(@Param("type") int i);
}
