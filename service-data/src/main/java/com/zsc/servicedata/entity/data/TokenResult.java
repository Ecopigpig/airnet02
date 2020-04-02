package com.zsc.servicedata.entity.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenResult implements Serializable {

    private static final long serialVersionUID = 6806117176467699319L;

    private Integer authStatus;

    private String apiToken;
}
