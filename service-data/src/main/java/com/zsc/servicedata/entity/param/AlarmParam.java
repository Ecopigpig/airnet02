package com.zsc.servicedata.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "检测是否该引发警报所需参数")
public class AlarmParam implements Serializable {

    private static final long serialVersionUID = -6446344551363540829L;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @NotEmpty(message = "城市名称不能为空")
    @ApiModelProperty(value = "城市名称")
    private String area;

    @NotEmpty(message = "监测点名称不能为空")
    @ApiModelProperty(value = "监测点名称")
    private String siteName;

    @NotEmpty(message = "污染物列表不能为空")
    @ApiModelProperty(value = "污染物列表")
    List<String> pollutantList;

}
