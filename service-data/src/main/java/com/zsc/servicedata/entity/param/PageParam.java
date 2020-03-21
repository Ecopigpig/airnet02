package com.zsc.servicedata.entity.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    private static final long serialVersionUID = 3514574012769326594L;
    private int page;
    private int size;
}
