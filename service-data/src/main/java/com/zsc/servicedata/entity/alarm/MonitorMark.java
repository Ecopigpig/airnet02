package com.zsc.servicedata.entity.alarm;

import lombok.Data;

import java.io.Serializable;

@Data
public class MonitorMark implements Serializable {

    private static final long serialVersionUID = 1950168518174940105L;

    private String area;

    private String alarmText;
}
