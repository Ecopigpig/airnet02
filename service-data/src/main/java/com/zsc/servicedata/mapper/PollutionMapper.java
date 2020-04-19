package com.zsc.servicedata.mapper;

import com.zsc.servicedata.entity.data.Pollutant;
import com.zsc.servicedata.entity.param.PollutionMonitorParam;
import model.air.HistoryAqiChart;
import model.pollutant.PollutionEpisode;
import model.pollutant.PollutionMonitorResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PollutionMapper {

//    @Select("select * from usertable order by user_id desc")
//    List<PollutionEpisode> getList();
//
//    @Select("select * from usertable order by user_id desc")
//    List<UserEntity> getUserList();
//
//    @Insert("insert into usertable(username,userpassword,age,sex) values(#{username},#{userpassword},#{age},#{sex})")
//    int AddUser(UserEntity user);

//    @Select("select * from pollutant where userId = #{userId}")
    List<Pollutant> selectMonitorListByUser(Long userId);

    void insertMonitorPoint(@Param("list") List<PollutionMonitorResult> paramList);

    List<Pollutant> selectAllMonitor();

    void insertPollutantHistory(@Param("list") List<PollutionEpisode> episodeList);

    List<PollutionEpisode> selectAllHistory();

    void insertAqiHistory(List<HistoryAqiChart> historyAqiChartList);

    int editMonitor(PollutionMonitorResult monitorResult);

    void deleteMonitor(@Param("id")Long id);
}
