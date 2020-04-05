package model.weather;

import lombok.Data;
import model.pollutant.PollutionEpisode;

import java.io.Serializable;

@Data
public class Reference implements Serializable {
    private static final long serialVersionUID = 8645180921238644053L;
    private String quality;
    private PollutionEpisode pollutionEpisode;
}
