package model.weather;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityCode implements Serializable {
    private static final long serialVersionUID = -4745647203481293058L;
    private String areaCode;
    private String postalCode;
}
