package com.zsc.servicedata.entity.alarm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 4560009488445282134L;

    private Long id;
    private Long userId;
    private String context;
    private Date sendTime;
    @ApiModelProperty(value = "站内信是否已读状态,0未读,1已读")
    private Byte isRead;
}
