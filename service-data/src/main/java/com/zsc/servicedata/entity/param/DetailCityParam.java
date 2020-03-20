package com.zsc.servicedata.entity.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class DetailCityParam implements Serializable {
    private static final long serialVersionUID = 3630257386146921994L;
    private String city;
    private String area;
}
