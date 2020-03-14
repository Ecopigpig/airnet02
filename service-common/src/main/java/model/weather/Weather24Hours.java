package model.weather;

import lombok.Data;

import java.io.Serializable;

@Data
public class Weather24Hours implements Serializable {
    /**
     * 地区名
     */
    private String area;
    /**
     * 天气编码
     */
    private String weatherCode;
    /**
     * 预报时间
     */
    private String time;
    /**
     * 风向
     */
    private String windDirection;
    /**
     * 风力
     */
    private String windPower;
    /**
     * 天气名称
     */
    private String weatherName;
    /**
     * 温度
     */
    private String temperature;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getWeatherName() {
        return weatherName;
    }

    public void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
