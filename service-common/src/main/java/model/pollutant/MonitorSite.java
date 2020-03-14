package model.pollutant;

import lombok.Data;

import java.io.Serializable;

/**
 * 城市下的污染检测点
 */
@Data
public class MonitorSite implements Serializable {

    private static final long serialVersionUID = -5503631266832059485L;

    private String area;

    private String siteName;

    private String latitude;

    private String longitude;
}
