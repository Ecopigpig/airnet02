package com.zsc.servicedata.service;

import com.zsc.servicedata.entity.param.AqiHistoryParam;
import model.air.HistoryAqiChart;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 空气质量排行榜
 */
@Service
public interface AirService {

    List<HistoryAqiChart> getAqiHistoryByRank(AqiHistoryParam param);

    List<HistoryAqiChart> getAqiHistoryByPollution(AqiHistoryParam param);
}
