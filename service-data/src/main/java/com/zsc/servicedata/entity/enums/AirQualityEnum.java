package com.zsc.servicedata.entity.enums;

public enum AirQualityEnum {

    HIGH_QUALITY(1, "优质"),

    FINE_QUALITY(2, "良好"),

    LIGHT_POLLUTION(3, "轻度污染"),

    MEDIUM_POLLUTION(4, "中度污染"),

    HEAVY_POLLUTION(4, "重度污染"),

    SERVIOUS_POLLUTION(4, "严重污染");


    private Integer code;

    private String desc;

    AirQualityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
