package com.zsc.servicehi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import model.weather.*;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class GetWeatherData {
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


    public List<Weather24Hours> get24HourWeather(String city) {
        List<Weather24Hours> weather24HoursList = new ArrayList<>();
        //请求地址设置
        String url = "http://api.apishop.net/common/weather/get24HoursWeatherByArea";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        params.put("area", city);
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String hourList = jsonResult.getString("hourList");
                System.out.println(hourList);
                List<Map<String, Object>> list = JSON.parseObject(hourList, List.class);
                for (Map<String, Object> objectMap : list) {
                    Weather24Hours weather24Hours = new Weather24Hours();
                    weather24Hours.setArea(objectMap.get("area").toString());
                    weather24Hours.setTemperature(objectMap.get("temperature").toString());
                    String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
                    String dateStr = objectMap.get("time").toString();
                    dateStr = dateStr.replaceAll(reg, "$1-$2-$3 $4:$5");
                    weather24Hours.setTime(dateStr);
                    weather24Hours.setWeatherName(objectMap.get("weather").toString());
                    weather24Hours.setWeatherCode(objectMap.get("weather_code").toString());
                    weather24Hours.setWindDirection(objectMap.get("wind_direction").toString());
                    weather24Hours.setWindPower(objectMap.get("wind_power").toString());
                    weather24HoursList.add(weather24Hours);
                }
            } else {
                return weather24HoursList;
            }
        } else {
            // 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
            return weather24HoursList;
        }
        return weather24HoursList;
    }

    public List<WeatherIn15Days> getWeatherIn15Days(String city) {
        List<WeatherIn15Days> weatherIn15DaysList = new ArrayList<>();
        //请求地址设置
        String url = "http://api.apishop.net/common/weather/get15DaysWeatherByArea";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        params.put("area", city);
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String hourList = jsonResult.getString("dayList");
                System.out.println(hourList);
                List<Map<String, Object>> list = JSON.parseObject(hourList, List.class);
                for (Map<String, Object> objectMap : list) {
                    WeatherIn15Days weatherIn15Days = new WeatherIn15Days();
                    weatherIn15Days.setArea(objectMap.get("area").toString());
                    weatherIn15Days.setDayAirTemperature(objectMap.get("day_air_temperature").toString());
                    weatherIn15Days.setDayWeather(objectMap.get("day_weather").toString());
                    weatherIn15Days.setDayWeatherPic(objectMap.get("day_weather_pic").toString());
                    weatherIn15Days.setDayWindDirection(objectMap.get("day_wind_direction").toString());
                    weatherIn15Days.setDayWindPower(objectMap.get("day_wind_power").toString());
                    weatherIn15Days.setNightAirTemperature(objectMap.get("night_air_temperature").toString());
                    weatherIn15Days.setNightWeather(objectMap.get("night_weather").toString());
                    weatherIn15Days.setNightWeatherPic(objectMap.get("night_weather_pic").toString());
                    weatherIn15Days.setNightWindDirection(objectMap.get("night_wind_direction").toString());
                    weatherIn15Days.setNightWindPower(objectMap.get("night_wind_power").toString());
                    String reg = "(\\d{4})(\\d{2})(\\d{2})";
                    String dateStr = objectMap.get("daytime").toString();
                    dateStr = dateStr.replaceAll(reg, "$1-$2-$3");
                    weatherIn15Days.setDayTime(dateStr);
                    weatherIn15DaysList.add(weatherIn15Days);
                }
            } else {
                // 状态码非000000, 说明请求失败
                return weatherIn15DaysList;
            }
        } else {
            // 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
            return weatherIn15DaysList;
        }
        return weatherIn15DaysList;
    }

    public List<AreaCode> getAreaCode(String city) {
        List<AreaCode> areaCodeList = new ArrayList<>();
        //请求地址设置
        String url = "http://api.apishop.net/common/weather/getAreaID";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        params.put("area", city);
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String codeList = jsonResult.getString("list");
                System.out.println(codeList);
                List<Map<String, JSONObject>> list = JSON.parseObject(codeList, List.class);
                for (Map<String, JSONObject> objectMap : list) {
                    AreaCode areaCode = new AreaCode();
                    JSONObject object = objectMap.get("cityInfo");
                    areaCode.setCity(object.getString("c5"));
                    areaCode.setArea(object.getString("c3"));
                    areaCode.setAreaCode(object.getString("c11"));
                    areaCode.setPostalCode(object.getString("c12"));
                    areaCodeList.add(areaCode);
                }

            } else {
                // 状态码非000000, 说明请求失败
                return areaCodeList;
            }
        } else {
            // 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
            return areaCodeList;
        }
        return areaCodeList;
    }

    public InstanceWeather getInstanceTimeWeather(String areaCode, String postalCode) {
        InstanceWeather instanceWeather = new InstanceWeather();
        //请求地址设置
        String url = "http://api.apishop.net/common/weather/getWeatherByPhonePostCode";
        //请求方法设置
        String requestMethod = "POST";
        //请求头部设置
        Map<String, String> headers = new HashMap<String, String>();
        //请求参数设置
        Map<String, String> params = new HashMap<String, String>();
        params.put("apiKey", "ElSPAFj03a93fd9040e1d65352247eb7c535f3f5ee5752c");
        params.put("need3HourForcast", "0");
        params.put("needAlarm", "1");
        params.put("needHourData", "0");
        params.put("needIndex", "1");
        params.put("needMoreDay", "0");
        params.put("phoneCode", areaCode);
        params.put("postCode", postalCode);
        String result = proxyToDesURL(requestMethod, url, headers, params);
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("statusCode");
            if (status_code.equals("000000")) {
                // 状态码为000000, 说明请求成功
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                String alarmList = jsonResult.getString("alarmList");
                JSONObject f1Result = jsonResult.getJSONObject("f1");
                JSONObject indexResult =f1Result.getJSONObject("index");
                instanceWeather.setAirPress(f1Result.getString("air_press"));
                String reg = "(\\d{4})(\\d{2})(\\d{2})";
                String dateStr = f1Result.getString("day");
                dateStr = dateStr.replaceAll(reg, "$1-$2-$3");
                instanceWeather.setDay(dateStr);
                instanceWeather.setDayTemperature(f1Result.getString("day_air_temperature"));
                instanceWeather.setDayWeather(f1Result.getString("day_weather"));
                instanceWeather.setDayWeatherPic(f1Result.getString("day_weather_pic"));
                instanceWeather.setNightTemperature(f1Result.getString("night_air_temperature"));
                instanceWeather.setNightWeather(f1Result.getString("night_weather"));
                instanceWeather.setNightWeatherPic(f1Result.getString("night_weather_pic"));
                instanceWeather.setRainRate(f1Result.getString("jiangshui"));
                instanceWeather.setSunBeginAndEnd(f1Result.getString("sun_begin_end"));
                instanceWeather.setWeekday(f1Result.getString("weekday"));
                //indexResult不是数组，只能一个一个对象取出来！！！！！！！！！！！！！
                String[] indexArray = {"ac","ag","aqi","beauty","cl","clothes","cold","uv","travel","ls"};
                WeatherTip weatherTip = new WeatherTip();
                for(int i =0;i<indexArray.length;i++){
                    JSONObject totalResult = indexResult.getJSONObject(indexArray[i]);
                    String titleResult = totalResult.getString("title");
                    String descResult = totalResult.getString("desc");
                    switch (indexArray[i]){
                        case "ac":
                            weatherTip.setAc("空调指数："+titleResult+"，"+descResult);
                            break;
                        case "ag":
                            weatherTip.setAg("过敏指数："+titleResult+"，"+descResult);
                            break;
                        case "aqi":
                            weatherTip.setAqi("空气指数："+titleResult+"，"+descResult);
                            break;
                        case "beauty":
                            weatherTip.setBeauty("化妆指数："+titleResult+"，"+descResult);
                            break;
                        case "cl":
                            weatherTip.setCl("晨练指数："+titleResult+"，"+descResult);
                            break;
                        case "clothes":
                            weatherTip.setClothes("穿衣指数："+titleResult+"，"+descResult);
                            break;
                        case "cold":
                            weatherTip.setCold("感冒指数："+titleResult+"，"+descResult);
                            break;
                        case "uv":
                            weatherTip.setUv("紫外线："+titleResult+"，"+descResult);
                            break;
                        case "travel":
                            weatherTip.setTravel("旅游指数："+titleResult+"，"+descResult);
                            break;
                        case "ls":
                            weatherTip.setLs("晾晒指数："+titleResult+"，"+descResult);
                            break;
                        default:
                            break;
                    }
                }
                instanceWeather.setWeatherTip(weatherTip);
                List<Map<String, Object>> aList = JSON.parseObject(alarmList, List.class);
                List<WeatherAlarm> weatherAlarmList = new ArrayList<>();
                for (Map<String, Object> objectMap : aList) {
                    WeatherAlarm weatherAlarm = new WeatherAlarm();
                    weatherAlarm.setIssueTime(objectMap.get("issueTime").toString());
                    weatherAlarm.setIssueContent(objectMap.get("issueContent").toString());
                    weatherAlarm.setSignalLevel(objectMap.get("signalLevel").toString());
                    weatherAlarm.setSignalType(objectMap.get("signalType").toString());
                    weatherAlarmList.add(weatherAlarm);
                }
                instanceWeather.setWeatherAlarmList(weatherAlarmList);
            } else {
                // 状态码非000000, 说明请求失败
                return instanceWeather;
            }
        } else {
            // 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
            return instanceWeather;
        }
        return instanceWeather;
    }
}
