package com.zsc.servicedata.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "数据库污染监测实体")
public class Pollutant implements Serializable {

    private static final long serialVersionUID = -6624379527740028333L;

    @Id
    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @NotEmpty(message = "城市名称不能为空")
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

    @NotEmpty
    @ApiModelProperty(value = "空气质量阈值")
    private String quality;

    @ApiModelProperty(value = "PM10阈值")
    private Float PM10;

    @ApiModelProperty(value = "每8小时的O3浓度阈值")
    private Float o3per8h;


}
