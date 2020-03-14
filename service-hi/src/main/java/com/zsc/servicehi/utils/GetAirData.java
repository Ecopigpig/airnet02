package com.zsc.servicehi.utils;

import com.alibaba.fastjson.JSONObject;
import model.air.AirQuality;
import model.pollutant.PollutionEpisode;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GetAirData {
    /**
     * 请求接口
     *
     * @param method  请求方法
     * @param url     请求地址
     * @param headers 请求头部
     * @param params  请求参数
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

    public AirQuality getAirQuality(String city) {
        AirQuality airQuality = new AirQuality();
        //请求地址设置
        String url = "http://api.apishop.net/common/air/getAirQualityByCity";
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
                PollutionEpisode pollutionEpisode = new PollutionEpisode();
                pollutionEpisode.setAqi(jsonResult.get("AQI").toString());
                pollutionEpisode.setCo(jsonResult.get("CO").toString());
                pollutionEpisode.setNo2(jsonResult.get("NO2").toString());
                pollutionEpisode.setO3(jsonResult.get("O3").toString());
                pollutionEpisode.setPm10(jsonResult.get("PM10").toString());
                pollutionEpisode.setPm25(jsonResult.get("PM2_5").toString());
                pollutionEpisode.setSo2(jsonResult.get("SO2").toString());
                pollutionEpisode.setCt(jsonResult.get("pubtime").toString());
                pollutionEpisode.setPrimaryPollutant(jsonResult.get("pollutions").toString());
                pollutionEpisode.setQuality(jsonResult.get("quality").toString());
                airQuality.setPollutionEpisode(pollutionEpisode);
                airQuality.setCity(jsonResult.get("city").toString());
                airQuality.setCityCode(jsonResult.get("city_code").toString());
                airQuality.setLatitude(jsonResult.get("latitude").toString());
                airQuality.setLongitude(jsonResult.get("longitude").toString());
                airQuality.setLevel(jsonResult.get("level").toString());
            } else {
                // 状态码非000000, 说明请求失败
                return airQuality;
            }
        } else {
            return airQuality;
        }
        return airQuality;
    }
}
