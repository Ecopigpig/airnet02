package model.pollutant;

import java.io.Serializable;

/**
 * 城市底下的污染检测点
 */
public class PollutionSite implements Serializable {

    private static final long serialVersionUID = -5667194913415998127L;

    /**
     * 污染检测点
     */
    private String siteName;

    /**
     * 基本污染指数模型
     */
    private PollutionEpisode pollutionEpisode;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public PollutionEpisode getPollutionEpisode() {
        return pollutionEpisode;
    }

    public void setPollutionEpisode(PollutionEpisode pollutionEpisode) {
        this.pollutionEpisode = pollutionEpisode;
    }
}
