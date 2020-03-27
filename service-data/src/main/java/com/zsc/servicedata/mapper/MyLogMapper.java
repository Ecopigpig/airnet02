package com.zsc.servicedata.mapper;

import model.log.Operation;
import org.springframework.stereotype.Component;

@Component
public interface MyLogMapper {

    void insertLog(Operation adminLog);
}
