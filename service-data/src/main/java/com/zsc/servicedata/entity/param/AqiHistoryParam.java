package com.zsc.servicedata.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import model.page.PageParam;

import java.io.Serializable;

@ApiModel(value = "AQI历史记录查询参数")
@Data
public class AqiHistoryParam extends PageParam implements Serializable {

    private static final long serialVersionUID = -587950169933189264L;

    @ApiModelProperty(value = "记录大小")
    private Long recordSize;

    @ApiModelProperty(value = "升降序,升序asc/降序desc")
    private String order;

    @ApiModelProperty(value = "查询起始时间")
    private String startTime;

    @ApiModelProperty(value = "查询结束时间")
    private String endTime;

    @ApiModelProperty(value = "空气质量筛选")
    private String quality;
}
