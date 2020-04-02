package com.zsc.servicedata.entity.data;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "用户申请调用API实体")
public class ApplyRecord implements Serializable {

    private static final long serialVersionUID = 4282838148877619073L;

    private Long id;
    private Long userId;
    private String reason;
    private Date applyTime;
    private String mail;
    private Integer auth;
}
