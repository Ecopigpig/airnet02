package com.zsc.servicedata.entity.param;

import lombok.Data;
import model.page.PageParam;

@Data
public class ApiParam extends PageParam {
    private static final long serialVersionUID = -4261095624374753731L;
    private Integer auth;
}
