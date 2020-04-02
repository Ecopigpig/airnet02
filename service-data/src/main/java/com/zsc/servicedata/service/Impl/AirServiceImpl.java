package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.entity.param.AqiHistoryParam;
import com.zsc.servicedata.mapper.AirMapper;
import com.zsc.servicedata.service.AirService;
import model.air.HistoryAqiChart;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("airService")
public class AirServiceImpl implements AirService {

    @Resource
    private AirMapper airMapper;

    @Override
    public List<HistoryAqiChart> getAqiHistoryByRank(AqiHistoryParam param) {
        return airMapper.selectAqiHistoryByRank(param);
    }

    @Override
    public List<HistoryAqiChart> getAqiHistoryByPollution(AqiHistoryParam param) {
        return airMapper.selectAqiHistoryByPollution(param);
    }
}
