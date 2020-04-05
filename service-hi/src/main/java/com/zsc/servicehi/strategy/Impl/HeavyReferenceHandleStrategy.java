package com.zsc.servicehi.strategy.Impl;

import com.zsc.servicehi.strategy.IReferenceHandleStrategy;
import model.pollutant.PollutionEpisode;
import model.weather.Reference;

public class HeavyReferenceHandleStrategy implements IReferenceHandleStrategy {
    @Override
    public Reference handleReference(String quality) {
        Reference reference = new Reference();
        reference.setQuality("重度污染");
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        pollutionEpisode.setAqi("201~300");
        pollutionEpisode.setSo2("-1");
        pollutionEpisode.setNo2("1201~2340");
        pollutionEpisode.setPm10("351~420");
        pollutionEpisode.setCo("61~90");
        pollutionEpisode.setO3("401~800");
        pollutionEpisode.setO3Per8h("266~800");
        pollutionEpisode.setPm25("151~250");
        reference.setPollutionEpisode(pollutionEpisode);
        return reference;
    }
}
