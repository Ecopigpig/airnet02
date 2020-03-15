package com.zsc.servicedata.utils.alarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsc.servicedata.entity.alarm.MonitorMark;
import com.zsc.servicedata.entity.data.Pollutant;
import com.zsc.servicedata.service.PollutionService;
import com.zsc.servicedata.service.UserService;
import com.zsc.servicedata.service.feign.HiFeignService;
import model.air.HistoryAqiChart;
import model.pollutant.PollutionEpisode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Schedule {

    @Resource
    JavaMailSender jms;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private PollutionService pollutionService;

    @Resource
    private UserService userService;

    @Autowired
    private HiFeignService hiFeignService;


    @Scheduled(fixedRate = 2000000)
    public void checkIsAlarm() throws IOException {
        //每隔半小时去获取实时数据，与数据库中用户设定作对比，超过预设值就发邮件
        //先从缓存里头获取全国实时排行榜，从排行榜里头获取所有城市的监测点的浓度
        String result = hiFeignService.offerNationPollutant();
//        String result = restTemplate.getForObject("http://localhost:8763/pollutant/offerNationPollutant", String.class);
//        String result = restTemplate.getForObject("http://service-hi:8763/pollutant/offerNationPollutant", String.class);
//        String result = restTemplate.getForObject("http://SERVICE-HI/pollutant/offerNationPollutant", String.class);
//        String result = restTemplate.getForObject("http://39.108.10.105:8763/pollutant/offerNationPollutant", String.class);
        List<PollutionEpisode> cityList = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(result);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            ObjectMapper objectMapper = new ObjectMapper();
            String str = job.getString("pollutionEpisode");
            PollutionEpisode city = objectMapper.readValue(str, PollutionEpisode.class);
            cityList.add(city);
        }
        //扫描数据库中各个用户的监测数据
        List<Pollutant> pollutantList = pollutionService.getAllMonitors();
        //记录需要发送邮件的用户ID
        Set<Long> userIdList = new HashSet<>();
        //记录用户所监测的城市和超过阈值的参数值
        //其中的key,String = id + area;
        Map<Long, MonitorMark> markMap = new HashMap<>();
        //数据库和实时数据进行比对
        cityList.forEach(city -> {
            for (Pollutant pollutant : pollutantList) {
                //是同一个城市,可以开始比对
                if (city.getArea().equals(pollutant.getArea())) {
                    //特殊的空气质量情况
                    int cityQualityNum = changeIntoQualityNum(city.getQuality());
                    int monitorQualityNum = changeIntoQualityNum(pollutant.getQuality());
                    if (cityQualityNum > monitorQualityNum) {
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "QUALITY");
                    }
                    if (Float.valueOf(city.getSo2()) > pollutant.getSo2()) {
                        //将该用户记录在需要发送邮件的名单中
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "SO2");
                    }
                    if (Float.valueOf(city.getO3()) > pollutant.getO3()) {
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "O3");
                    }
                    if (city.getPm25().equals("_")) {
                        city.setPm25("0.0");
                        if (Float.valueOf(city.getPm25()) > pollutant.getPm25()) {
                            userIdList.add(pollutant.getUserId());
                            markDownOverData(markMap, city, pollutant, "PM25");
                        }
                    }
                    if (Float.valueOf(city.getCo()) > pollutant.getCo()) {
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "CO");
                    }
                    if (Float.valueOf(city.getNo2()) > pollutant.getNo2()) {
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "NO2");
                    }
                    if (Float.valueOf(city.getAqi()) > pollutant.getAqi()) {
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "AQI");
                    }
                    if (city.getPm10().equals("_")) {
                        city.setPm10("0.0");
                        if (Float.valueOf(city.getPm10()) > pollutant.getPM10()) {
                            userIdList.add(pollutant.getUserId());
                            markDownOverData(markMap, city, pollutant, "PM10");
                        }
                    }
                    if (Float.valueOf(city.getO3Per8h()) > pollutant.getO3per8h()) {
                        userIdList.add(pollutant.getUserId());
                        markDownOverData(markMap, city, pollutant, "O3PER8H");
                    }
                }
            }
        });
        //发邮件
        //根据用户ID,批量获取其邮件地址
        Map<Long, String> map = userService.getAllUserEmail(userIdList);
        for (Long id : map.keySet()) {
            for (Long userId : markMap.keySet()) {
                if (id.equals(userId)) {
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
//                    mailMessage.setFrom("Ecochoupipig@163.com");
                    mailMessage.setFrom("1315943577@qq.com");
                    mailMessage.setTo(map.get(id));
                    mailMessage.setSubject("报警邮件");
                    MonitorMark monitorMark = markMap.get(userId);
                    mailMessage.setText(monitorMark.getAlarmText());
                    jms.send(mailMessage);
                }
            }
        }
    }


    private static int changeIntoQualityNum(String quality) {
        int qualityNum = 0;
        switch (quality) {
            case "优质":
                qualityNum = 1;
                break;
            case "良好":
                qualityNum = 2;
                break;
            case "轻度污染":
                qualityNum = 3;
                break;
            case "中度污染":
                qualityNum = 4;
                break;
            case "重度污染":
                qualityNum = 5;
                break;
            case "严重污染":
                qualityNum = 6;
                break;
            default:
                break;
        }
        return qualityNum;
    }

    private static Map<Long, MonitorMark> markDownOverData(Map<Long, MonitorMark> markMap, PollutionEpisode city, Pollutant pollutant, String pollutantName) {
        MonitorMark monitorMark = new MonitorMark();
        monitorMark.setArea(pollutant.getArea());
        StringBuilder stringBuilder = new StringBuilder();
        switch (pollutantName) {
            case "QUALITY": {
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前空气质量：" + city.getQuality() + "，超过了您所设置的警报阈值：" + pollutant.getQuality() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前空气质量：" + city.getQuality() + "，超过了您所设置的警报阈值：" + pollutant.getQuality() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            }
            case "SO2": {
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前SO2的1小时平均浓度：" + city.getSo2() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getSo2() + "\n");
                } else {
                    //这里要解决一个用户，多个城市问题，应该还有一个城市，多个用户的问题
                    for (Long userId : markMap.keySet()) {
                        //缓存里有这个ID，就直接append
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前SO2的1小时平均浓度：" + city.getSo2() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getSo2() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            }
            case "O3": {
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前O3的1小时平均浓度：" + city.getO3() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getO3() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前O3的1小时平均浓度：" + city.getO3() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getO3() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            }
            case "PM25":
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前pm2.5的1小时平均浓度：" + city.getPm25() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getPm25() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前pm2.5的1小时平均浓度：" + city.getPm25() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getPm25() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            case "CO":
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前pm2.5的1小时平均浓度：" + city.getPm25() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getPm25() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前pm2.5的1小时平均浓度：" + city.getPm25() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getPm25() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            case "NO2":
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前NO2浓度的1小时平均浓度：" + city.getNo2() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getNo2() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前NO2浓度的1小时平均浓度：" + city.getNo2() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getNo2() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            case "AQI":
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前AQI指数：" + city.getAqi() + "，超过了您所设置的警报阈值：" + pollutant.getAqi() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前AQI指数：" + city.getAqi() + "，超过了您所设置的警报阈值：" + pollutant.getAqi() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            case "PM10":
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前pm10的1小时平均浓度：" + city.getQuality() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getPM10() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前pm10的1小时平均浓度：" + city.getQuality() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getPM10() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            case "O3PER8H":
                if (markMap.isEmpty()) {
                    stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前O3的8小时滑动平均浓度：" + city.getO3Per8h() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getO3per8h() + "\n");
                } else {
                    for (Long userId : markMap.keySet()) {
                        if (userId.equals(pollutant.getUserId())) {
                            String existedStr = markMap.get(pollutant.getUserId()).getAlarmText() + "\n";
                            stringBuilder.append(existedStr);
                            stringBuilder.append("您所监测的城市：" + pollutant.getArea() + "，当前O3的8小时滑动平均浓度：" + city.getO3Per8h() + " ug/m3，超过了您所设置的警报阈值：" + pollutant.getO3per8h() + "\n");
                        }
                    }
                }
                monitorMark.setAlarmText(stringBuilder.toString());
                break;
            default:
                break;
        }
        markMap.put(pollutant.getUserId(), monitorMark);
        return markMap;
    }

    /**
     * 定时记录367个城市的污染情况 和 空气质量情况
     * @throws IOException
     */
    @Scheduled(fixedRate = 2000000)
//    @Scheduled(cron = "0 0 20 * * ?")// 每天下午8点
    public void markPollutantHistory() throws IOException, ParseException {
        String result = hiFeignService.offerNationPollutant();
//        String result = restTemplate.getForObject("http://localhost:8763/pollutant/offerNationPollutant", String.class);
//        String result = restTemplate.getForObject("http://service-hi:8763/pollutant/offerNationPollutant", String.class);
//        String result = restTemplate.getForObject("http://SERVICE-HI/pollutant/offerNationPollutant", String.class);
//        String result = restTemplate.getForObject("http://39.108.10.105:8763/pollutant/offerNationPollutant", String.class);
        List<PollutionEpisode> cityList = new ArrayList<>();
        List<HistoryAqiChart> historyAqiChartList = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseArray(result);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            ObjectMapper objectMapper = new ObjectMapper();
            String str = job.getString("pollutionEpisode");
            String rank = job.getString("num");
            PollutionEpisode city = objectMapper.readValue(str, PollutionEpisode.class);
            city.setRank(Integer.valueOf(rank));
            HistoryAqiChart historyAqiChart = new HistoryAqiChart();
            historyAqiChart.setCity(city.getArea());
            historyAqiChart.setAqi(city.getAqi());
            historyAqiChart.setQuality(city.getQuality());
            historyAqiChart.setRank(city.getRank());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateStr = sdf.format(new Date());
            Date date = sdf.parse(dateStr);
            historyAqiChart.setMarkTime(date);
            cityList.add(city);
            historyAqiChartList.add(historyAqiChart);
        }
        pollutionService.markPollutantHistory(cityList);
        pollutionService.markAqiHistory(historyAqiChartList);
    }
}
