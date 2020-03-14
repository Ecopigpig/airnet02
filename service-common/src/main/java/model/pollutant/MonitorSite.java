package model.pollutant;

import lombok.Data;

/**
 * 城市下的污染检测点
 */
@Data
public class MonitorSite {

    private String area;

    private String siteName;

    private String latitude;

    private String longitude;
}
