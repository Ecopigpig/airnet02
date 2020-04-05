package com.zsc.servicehi.strategy.Impl;

import com.zsc.servicehi.strategy.IReferenceHandleStrategy;
import model.pollutant.PollutionEpisode;
import model.weather.Reference;

public class SeriousReferenceHandleStrategy implements IReferenceHandleStrategy {
    @Override
    public Reference handleReference(String quality) {
        Reference reference = new Reference();
        reference.setQuality("严重污染");
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        pollutionEpisode.setAqi(">300");
        pollutionEpisode.setSo2("-1");
        pollutionEpisode.setNo2("2341~3090");
        pollutionEpisode.setPm10("421~500");
        pollutionEpisode.setCo("91~120");
        pollutionEpisode.setO3("801~1000");
        pollutionEpisode.setO3Per8h("-2");
        pollutionEpisode.setPm25("251~350");
        reference.setPollutionEpisode(pollutionEpisode);
        return reference;
    }
}
