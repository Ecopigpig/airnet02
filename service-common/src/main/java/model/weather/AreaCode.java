package model.weather;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "城市代码表对应实体")
public class AreaCode implements Serializable {

    private static final long serialVersionUID = 4766082220125166390L;

    private Long id;
    @ApiModelProperty(value = "所在市名")
    private String city;
    @ApiModelProperty(value = "所在城市名")
    private String area;
    @ApiModelProperty(value = "城市区号")
    private String areaCode;
    @ApiModelProperty(value = "城市邮编")
    private String postalCode;

}
