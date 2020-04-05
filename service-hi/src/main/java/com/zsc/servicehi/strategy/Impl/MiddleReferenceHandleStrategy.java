package com.zsc.servicehi.strategy.Impl;

import com.zsc.servicehi.strategy.IReferenceHandleStrategy;
import model.pollutant.PollutionEpisode;
import model.weather.Reference;

public class MiddleReferenceHandleStrategy implements IReferenceHandleStrategy {
    @Override
    public Reference handleReference(String quality) {
        Reference reference = new Reference();
        reference.setQuality("中度污染");
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        pollutionEpisode.setAqi("151~200");
        pollutionEpisode.setSo2("651~800");
        pollutionEpisode.setNo2("701~1200");
        pollutionEpisode.setPm10("251~350");
        pollutionEpisode.setCo("36~60");
        pollutionEpisode.setO3("301~400");
        pollutionEpisode.setO3Per8h("216~265");
        pollutionEpisode.setPm25("116~150");
        reference.setPollutionEpisode(pollutionEpisode);
        return reference;
    }
}
