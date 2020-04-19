package com.zsc.servicehi.strategy.Impl;

import com.zsc.servicehi.strategy.IReferenceHandleStrategy;
import model.pollutant.PollutionEpisode;
import model.weather.Reference;

public class FineReferenceHandleStrategy implements IReferenceHandleStrategy {
    @Override
    public Reference handleReference(String quality) {
        Reference reference = new Reference();
        reference.setQuality("良好");
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        pollutionEpisode.setAqi("51~100");
        pollutionEpisode.setSo2("151~500");
        pollutionEpisode.setNo2("101~200");
        pollutionEpisode.setPm10("51~150");
        pollutionEpisode.setCo("6~10");
        pollutionEpisode.setO3("161~200");
        pollutionEpisode.setO3Per8h("101~160");
        pollutionEpisode.setPm25("36~75");
        reference.setPollutionEpisode(pollutionEpisode);
        return reference;
    }
}
