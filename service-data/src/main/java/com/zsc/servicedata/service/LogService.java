package com.zsc.servicedata.service;

import model.log.Operation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogService {
    List<Operation> getLogList();

    int getLoginUserNum();

    int getLogTotal();
}
