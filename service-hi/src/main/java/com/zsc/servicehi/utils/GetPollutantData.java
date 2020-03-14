package com.zsc.servicehi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import model.pollutant.MonitorSite;
import model.pollutant.PollutantCity;
import model.pollutant.PollutionEpisode;
import model.pollutant.PollutionSite;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class GetPollutantData {
    /**
     * 请求接口
     * @param method 请求方法
     * @param url 请求地址
     * @param headers 请求头部
     * @param params 请求参数
     * @return
     */
    private static String proxyToDesURL(String method, String url, Map<String, String> headers,
                                       Map<String, String> params) {
        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            //处理请求头部
            HttpHeaders requestHeaders = new HttpHeaders();
            if (headers != null && !headers.isEmpty()) {
                Set<String> set = headers.keySet();
                for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    String value = headers.get(key);
                    requestHeaders.add(key, value);
                }
            }
            //处理请求参数
            MultiValueMap<String, String> paramList = new LinkedMultiValueMap<String, String>();
            if (params != null && !params.isEmpty()) {
                if (method.equalsIgnoreCase("GET")) {
                    url += "?";
                    Set<String> set = params.keySet();
                    for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                        String key = iterator.next();
                        String value = params.get(key);
                        url += key + "=" + value + "&";
                    }
                    url = url.substring(0, url.length() - 1);
                } else {
                    Set<String> set = params.keySet();
                    for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                        String key = iterator.next();
                        String value = params.get(key);
                        paramList.add(key, value);
                    }
                }
            }
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
                    paramList, requestHeaders);
            //处理请求方法
            HttpMethod requestType = HttpMethod.GET;
            method = method.toUpperCase();
            switch (method) {
                case "GET":
                    requestType = HttpMethod.GET;
                    break;
                case "POST":
                    requestType = HttpMethod.POST;
                    break;
                case "PUT":
                    requestType = HttpMethod.PUT;
                    break;
                case "DELETE":
                    requestType = HttpMethod.DELETE;
                    break;
                case "HEAD":
                    requestType = HttpMethod.HEAD;
                    break;
                case "OPTIONS":
                    requestType = HttpMethod.OPTIONS;
                    break;
                default:
                    requestType = HttpMethod.GET;
                    break;
            }
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, requestType, requestEntity,
                    String.class, params);
            //获取返回结果
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    /**
//     * 主函数
//     * @param args
//     */
//    public static void main(String args[]) {
//        //请求地址设置
//        String url = "http://api.apishop.net/common/air/getCityPM25Detail";
//        //请求方法设置
//        String requestMethod = "POST";
//        //请求头部设置
//        Map<String, String> headers = new HashMap<String, String>();
//        //请求参数设置
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
//        params.put("city", "广州市");
//        String result = proxyToDesURL(requestMethod, url, headers, params);
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        JSONObject jsonResult = jsonObject.getJSONObject("result");
//        //注意：family中的内容带有中括号[]，所以要转化为JSONArray类型的对象
//        String siteList = jsonResult.getString("siteList");
//        List<Map<String,Object>> list=JSON.parseObject(siteList,List.class);
//        for (Map<String, Object> stringStringMap : list) {
//            String siteName=stringStringMap.get("site_name").toString();
//            System.out.println(siteName);
//        }
//    }

    public PollutantCity getCityPollutionEpisode(String city){
        PollutantCity pollutantCity = new PollutantCity();
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        List<PollutionSite> pollutionSiteList = new ArrayList<>();
        //请求地址设置
        String url = "http://api.apishop.net/common/air/getCityPM25Detail";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        params.put("city", city);
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String siteList = jsonResult.getString("siteList");
                List<Map<String,Object>> list=JSON.parseObject(siteList,List.class);
                for (Map<String, Object> objectMap : list) {
                    PollutionSite pollutionSite = new PollutionSite();
                    PollutionEpisode episode = new PollutionEpisode();
                    episode.setCt(objectMap.get("ct").toString());
                    episode.setO3Per8h(objectMap.get("o3_8h").toString());
                    episode.setO3(objectMap.get("o3").toString());
                    episode.setPrimaryPollutant(objectMap.get("primary_pollutant").toString());
                    episode.setAqi(objectMap.get("aqi").toString());
                    episode.setSo2(objectMap.get("so2").toString());
                    episode.setPm25(objectMap.get("pm2_5").toString());
                    episode.setNo2(objectMap.get("no2").toString());
                    episode.setQuality(objectMap.get("quality").toString());
                    episode.setCo(objectMap.get("co").toString());
                    episode.setPm10(objectMap.get("pm10").toString());
                    pollutionSite.setPollutionEpisode(episode);
                    pollutionSite.setSiteName(objectMap.get("site_name").toString());
                    pollutionSiteList.add(pollutionSite);
                }
                pollutantCity.setPollutionSiteList(pollutionSiteList);
                String str = jsonResult.getString("pm");
                String jsonPm = str.substring(1,str.length()-1);
                System.out.println("请求成功：" + jsonPm);
                String[] str2 = jsonPm.split(",");
                Map<String,String> map = new HashMap<>();
                for(int i = 0 ; i<str2.length;i++){
                    String[] str3 = str2[i].split(":");
                    if(i==8){
                        String dateStr = str3[1]+":"+str3[2]+":"+str3[3];
                        map.put(str3[0].substring(1,str3[0].length()-1),dateStr.substring(1,dateStr.length()-1));
                    }else {
                        map.put(str3[0].substring(1, str3[0].length() - 1), str3[1].substring(1, str3[1].length() - 1));
                    }
                }
                for (String key : map.keySet()) {
                    switch (key){
                        case "area":
                            pollutionEpisode.setArea(map.get("area"));
                        case "o3":
                            pollutionEpisode.setO3(map.get("o3"));
                        case "pm10":
                            pollutionEpisode.setPm10(map.get("pm10"));
                        case "co":
                            pollutionEpisode.setCo(map.get("co"));
                        case "quality":
                            pollutionEpisode.setQuality(map.get("quality"));
                        case "no2":
                            pollutionEpisode.setNo2(map.get("no2"));
                        case "pm2_5":
                            pollutionEpisode.setPm25(map.get("pm2_5"));
                        case "so2":
                            pollutionEpisode.setSo2(map.get("so2"));
                        case "aqi":
                            pollutionEpisode.setAqi(map.get("aqi"));
                        case "primary_pollutant":
                            pollutionEpisode.setPrimaryPollutant(map.get("primary_pollutant"));
                        case "o3_8h":
                            pollutionEpisode.setO3Per8h(map.get("o3_8h"));
                        case "num":
                            pollutantCity.setNum(map.get("num"));
                        case "ct":
                            pollutionEpisode.setCt(map.get("ct"));
                        default:
                            break;
                    }
                }
                pollutantCity.setPollutionEpisode(pollutionEpisode);
            } else {
                // 状态码非000000, 说明请求失败
                return pollutantCity;
            }
        } else {
            return pollutantCity;
        }
        return pollutantCity;
    }


    /**
     * //todo 此处应该分页，但现在pageHelper无法引入
     * @return
     */
    public List<PollutantCity> getNationPollutantRank(){
        List<PollutantCity> pollutantCityList = new ArrayList<>();
        //请求地址设置
        String url = "http://api.apishop.net/common/air/getPM25Top";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String resultList = jsonResult.getString("list");
                List<Map<String,Object>> list=JSON.parseObject(resultList,List.class);
                for (Map<String, Object> objectMap : list) {
                    PollutantCity pollutantCity = new PollutantCity();
                    pollutantCity.setNum(objectMap.get("num").toString());
                    PollutionEpisode episode = new PollutionEpisode();
                    episode.setCt(objectMap.get("ct").toString());
                    episode.setO3Per8h(objectMap.get("o3_8h").toString());
                    episode.setO3(objectMap.get("o3").toString());
                    episode.setPrimaryPollutant(objectMap.get("primary_pollutant").toString());
                    episode.setAqi(objectMap.get("aqi").toString());
                    episode.setSo2(objectMap.get("so2").toString());
                    episode.setPm25(objectMap.get("pm2_5").toString());
                    episode.setNo2(objectMap.get("no2").toString());
                    episode.setQuality(objectMap.get("quality").toString());
                    episode.setCo(objectMap.get("co").toString());
                    episode.setPm10(objectMap.get("pm10").toString());
                    episode.setArea(objectMap.get("area").toString());
                    pollutantCity.setPollutionEpisode(episode);
                    pollutantCityList.add(pollutantCity);
                }
            } else {
                // 状态码非000000, 说明请求失败
                return pollutantCityList;
            }
        } else {
            return pollutantCityList;
        }
        return pollutantCityList;

    }

    public List<MonitorSite> getSitesWithLocation(){
        List<MonitorSite> monitorSiteList = new ArrayList<>();
        //请求地址设置
        String url = "http://api.apishop.net/common/air/getStationList";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    MonitorSite monitorSite = new MonitorSite();
                    monitorSite.setArea(object.getString("city"));
                    monitorSite.setSiteName(object.getString("station"));
                    monitorSite.setLongitude(object.getString("lng"));
                    monitorSite.setLatitude(object.getString("lat"));
                    monitorSiteList.add(monitorSite);
                }
            } else {
                // 状态码非000000, 说明请求失败
                return monitorSiteList;
            }
        } else {
            return monitorSiteList;
        }
        return monitorSiteList;
    }

//    @Override
//    public PageInfo<Users> findAllUsers(int page, int size) {
//        PageHelper.startPage(page,size);
//        PageHelper.orderBy("id desc");//userId从大到小排列
//        PageInfo<Users> pageInfo=new PageInfo<>(usersDao.selectAllUsers());
//        return pageInfo;
//    }

}
