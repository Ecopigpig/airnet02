package model.weather;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "未来15天天气情况实体")
public class WeatherIn15Days {

    @ApiModelProperty(value = "地区名称")
    private String area;

    @ApiModelProperty(value = "日平均温度")
    private String dayAirTemperature;

    @ApiModelProperty(value = "天气状况")
    private String dayWeather;

    @ApiModelProperty(value = "天气状态示例图片")
    private String dayWeatherPic;

    @ApiModelProperty(value = "风向")
    private String dayWindDirection;

    @ApiModelProperty(value = "风力")
    private String dayWindPower;

    @ApiModelProperty(value = "日期")
    private String dayTime;

    @ApiModelProperty(value = "晚间温度")
    private String nightAirTemperature;

    @ApiModelProperty(value = "晚间天气状况")
    private String nightWeather;

    @ApiModelProperty(value = "晚间天气状况示例图片")
    private String nightWeatherPic;

    @ApiModelProperty(value = "晚间风向")
    private String nightWindDirection;

    @ApiModelProperty(value = "晚间风力")
    private String nightWindPower;

}
