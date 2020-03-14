package model.air;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import model.pollutant.PollutionEpisode;

import java.io.Serializable;

@Data
@ApiModel(value = "城市空气质量")
public class AirQuality implements Serializable {

    private static final long serialVersionUID = 1840221730181099950L;

    @ApiModelProperty(value = "基本污染指数模型")
    private PollutionEpisode pollutionEpisode;

    @ApiModelProperty(value = "城市名称")
    private String city;

    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "空气质量指数类别，有“优、良、轻度污染、中度污染、重度污染、严重污染”6类")
    private String Level;

    @ApiModelProperty(value = "经度")
    private String longitude;
}
