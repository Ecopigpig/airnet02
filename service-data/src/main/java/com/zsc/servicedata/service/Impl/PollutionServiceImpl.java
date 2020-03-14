package com.zsc.servicedata.service.Impl;

import com.zsc.servicedata.entity.data.Pollutant;
import com.zsc.servicedata.entity.param.PollutionMonitorParam;
import com.zsc.servicedata.mapper.CityMapper;
import com.zsc.servicedata.mapper.PollutionMapper;
import com.zsc.servicedata.service.PollutionService;
import model.air.HistoryAqiChart;
import model.pollutant.MonitorSite;
import model.pollutant.PollutionEpisode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service("pollutionService")
public class PollutionServiceImpl implements PollutionService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private PollutionMapper pollutionMapper;

    @Resource
    private CityMapper cityMapper;


    @Override
    public List<Pollutant> getMonitorListByUser(Long userId) {
        return pollutionMapper.selectMonitorListByUser(userId);
    }

    @Override
    public void setMonitor(List<PollutionMonitorParam> paramList)throws Exception{
            paramList.forEach(param -> {
                if(param.getArea().equals("")||param.getArea()==null){
                    try {
                        throw new Exception("城市名称不能为空");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        pollutionMapper.insertMonitorPoint(paramList);
    }

    @Override
    public List<MonitorSite> getMonitorPointInCity(String city) {
        //先获取城市的监测点
        List<MonitorSite> monitorSiteList = cityMapper.selectMonitorSiteByCity(city);
        return monitorSiteList;
    }

    @Override
    public List<Pollutant> getAllMonitors() {
        return pollutionMapper.selectAllMonitor();
    }

    @Override
    public void markPollutantHistory(List<PollutionEpisode> cityList) {
        pollutionMapper.insertPollutantHistory(cityList);
    }

    @Override
    public List<PollutionEpisode> getPollutantHistory() {
        return pollutionMapper.selectAllHistory();
    }

    @Override
    public void markAqiHistory(List<HistoryAqiChart> historyAqiChartList) {
        pollutionMapper.insertAqiHistory(historyAqiChartList);
    }
}
