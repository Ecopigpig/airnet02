package model.pollutant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "用户添加城市污染物检测指标参数")
public class PollutionMonitorResult implements Serializable {

    private static final long serialVersionUID = 7709966672579278660L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "要检测的城市名称")
    private String area;

    @ApiModelProperty(value = "SO2浓度阈值")
    private Float so2;

    @ApiModelProperty(value = "O3浓度阈值")
    private Float o3;

    @ApiModelProperty(value = "PM2.5浓度阈值")
    private Float pm25;

    @ApiModelProperty(value = "CO浓度阈值")
    private Float co;

    @ApiModelProperty(value = "NO2浓度阈值")
    private Float no2;

    @ApiModelProperty(value = "AQI浓度阈值")
    private Float aqi;

    @ApiModelProperty(value = "空气质量阈值,有“优、良、轻度污染、中度污染、重度污染、严重污染”6类")
    private String quality;

    @ApiModelProperty(value = "PM10阈值")
    private Float pm10;

    @ApiModelProperty(value = "每8小时的O3浓度阈值")
    private Float o3per8h;
}
