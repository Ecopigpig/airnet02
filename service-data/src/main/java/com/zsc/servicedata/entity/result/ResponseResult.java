package com.zsc.servicedata.entity.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = -4517314806161267570L;

    private Boolean msg;

    private Object data;

    private Long total;

}
