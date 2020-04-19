package com.zsc.servicedata.service;

import com.zsc.servicedata.entity.data.Pollutant;
import com.zsc.servicedata.entity.param.PollutionMonitorParam;
import model.air.HistoryAqiChart;
import model.pollutant.MonitorSite;
import model.pollutant.PollutionEpisode;
import model.pollutant.PollutionMonitorResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PollutionService {

    /**
     * 根据用户ID获取其检测点
     * @param userId
     * @return
     */
    List<Pollutant> getMonitorListByUser(Long userId);

    /**
     * 设置监测点
     * @param paramList
     * @return
     */
    void setMonitor(List<PollutionMonitorResult> paramList)throws Exception;

    /**
     * 通过城市名返回该城市下的污染检测点
     * @param city
     * @return
     */
    List<MonitorSite> getMonitorPointInCity(String city);

    /**
     * 获取所有的监测记录
     * @return
     */
    List<Pollutant> getAllMonitors();

    /**
     * 把全国城市的污染情况记录在数据库中,称为历史记录
     * @param cityList
     */
    void markPollutantHistory(List<PollutionEpisode> cityList);

    /**
     * 获取数据库中记录的367个城市的历史污染情况
     * @return
     */
    List<PollutionEpisode> getPollutantHistory();

    /**
     * 把全国城市的空气质量记录在数据库中,称为历史记录
     * @param historyAqiChartList
     */
    void markAqiHistory(List<HistoryAqiChart> historyAqiChartList);

    int editMonitor(PollutionMonitorResult monitorResult);

    void deleteMonitor(Long id);
}
