package model.weather;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "生活提示")
public class WeatherTip implements Serializable {

    private static final long serialVersionUID = -9032575514472447124L;

    @ApiModelProperty(value ="空调指数" )
    private String ac;

    @ApiModelProperty(value ="过敏指数" )
    private String ag;

    @ApiModelProperty(value ="空气指数" )
    private String aqi;

    @ApiModelProperty(value ="化妆指数" )
    private String beauty;

    @ApiModelProperty(value ="晨练指数" )
    private String cl;

    @ApiModelProperty(value ="穿衣指数" )
    private String clothes;

    @ApiModelProperty(value ="感冒指数" )
    private String cold;

    @ApiModelProperty(value ="紫外线" )
    private String uv;

    @ApiModelProperty(value ="旅游指数" )
    private String travel;

    @ApiModelProperty(value ="晾晒指数" )
    private String ls;



}
