package model.weather;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "天气预警模型")
public class WeatherAlarm implements Serializable {

    private static final long serialVersionUID = 5768717304677923768L;

//    @ApiModelProperty(value = "城市名")
//    private String city;

    @ApiModelProperty(value = "预警内容")
    private String issueContent;

    @ApiModelProperty(value = "预警发布时间")
    private String issueTime;

//    @ApiModelProperty(value = "省份")
//    private String province;

    @ApiModelProperty(value = "预警程度")
    private String signalLevel;

    @ApiModelProperty(value = "预警名称")
    private String signalType;



}
