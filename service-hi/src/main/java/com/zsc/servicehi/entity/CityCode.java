package com.zsc.servicehi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityCode implements Serializable {
    private static final long serialVersionUID = 5032147716058448582L;
    private String areaCode;
    private String postalCode;
}
