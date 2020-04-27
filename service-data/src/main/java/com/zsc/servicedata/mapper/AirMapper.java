package com.zsc.servicedata.mapper;

import com.zsc.servicedata.entity.param.AqiHistoryParam;
import model.air.HistoryAqiChart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AirMapper {

    List<HistoryAqiChart> selectAqiHistoryByRank(AqiHistoryParam param);

    List<HistoryAqiChart> selectAqiHistoryByPollution(AqiHistoryParam param);

    List<HistoryAqiChart> exportAqiHistoryByPollution(AqiHistoryParam param);
}
