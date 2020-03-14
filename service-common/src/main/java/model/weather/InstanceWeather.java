package model.weather;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "实时天气基本信息")
public class InstanceWeather implements Serializable {

    private static final long serialVersionUID = 6134821832177151799L;

    @ApiModelProperty(value = "城市名")
    private String city;

    @ApiModelProperty(value = "大气压")
    private String airPress;

    @ApiModelProperty(value = "降水概率")
    private String rainRate;

    @ApiModelProperty(value = "当前日期")
    private String day;

    @ApiModelProperty(value = "日间温度")
    private String dayTemperature;

    @ApiModelProperty(value = "白天天气")
    private String dayWeather;

    @ApiModelProperty(value = "白天天气图")
    private String dayWeatherPic;

    @ApiModelProperty(value = "晚间温度")
    private String NightTemperature;

    @ApiModelProperty(value = "晚上天气")
    private String nightWeather;

    @ApiModelProperty(value = "晚上天气图")
    private String nightWeatherPic;

    @ApiModelProperty(value = "星期几")
    private String weekday;

    @ApiModelProperty(value = "日出日落时间")
    private String sunBeginAndEnd;

    @ApiModelProperty(value = "预警信息")
    private List<WeatherAlarm> weatherAlarmList;

    @ApiModelProperty(value = "生活提示")
    private WeatherTip weatherTip;

}
