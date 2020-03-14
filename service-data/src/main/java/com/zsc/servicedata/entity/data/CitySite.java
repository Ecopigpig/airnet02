package com.zsc.servicedata.entity.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "城市对应检测")
public class CitySite implements Serializable {

    private static final long serialVersionUID = 7094134810630877018L;

    @ApiModelProperty(value = "城市名")
    private String area;

    @ApiModelProperty(value = "监测点名称")
    private String siteName;


}
