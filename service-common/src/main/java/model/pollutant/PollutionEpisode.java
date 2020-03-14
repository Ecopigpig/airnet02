package model.pollutant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "基本污染指数模型")
public class PollutionEpisode {

    private Long id;

    @ApiModelProperty(value = "二氧化硫1小时平均浓度，ug/m3")
    private String so2;

    @ApiModelProperty(value = "臭氧1小时平均，μg/m3")
    private String o3;

    @ApiModelProperty(value = "颗粒物（粒径小于等于2.5μm）1小时平均，μg/m3")
    private String pm25;

    @ApiModelProperty(value = "发布时间")
    private String ct;

    @ApiModelProperty(value = "首要污染物")
    private String primaryPollutant;

    @ApiModelProperty(value = "一氧化碳1小时平均，mg/m3")
    private String co;

    @ApiModelProperty(value = "城市名称")
    private String area;

    @ApiModelProperty(value = "二氧化氮1小时平均，μg/m3")
    private String no2;

    @ApiModelProperty(value = "空气质量指数(AQI)")
    private String aqi;

    @ApiModelProperty(value = "空气质量指数类别，有“优、良、轻度污染、中度污染、重度污染、严重污染”6类")
    private String quality;

    @ApiModelProperty(value = "颗粒物（粒径小于等于10μm）1小时平均，μg/m3")
    private String pm10;

    @ApiModelProperty(value = "臭氧8小时滑动平均，μg/m3")
    private String o3Per8h;

    @ApiModelProperty(value = "排名")
    private Integer rank;

}
