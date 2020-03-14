package model.air;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "空气质量历史排行榜")
public class HistoryAqiChart implements Serializable {

    private static final long serialVersionUID = 3199605270676912889L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "记录时间")
    private Date markTime;

    @ApiModelProperty(value = "城市名称")
    private String city;

    @ApiModelProperty(value = "城市排名")
    private Integer rank;

    @ApiModelProperty(value = "AQI指数")
    private String aqi;

    @ApiModelProperty(value = "污染情况")
    private String quality;
}
