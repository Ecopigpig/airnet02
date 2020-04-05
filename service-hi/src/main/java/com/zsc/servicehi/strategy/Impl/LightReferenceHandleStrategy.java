package com.zsc.servicehi.strategy.Impl;

import com.zsc.servicehi.strategy.IReferenceHandleStrategy;
import model.pollutant.PollutionEpisode;
import model.weather.Reference;

public class LightReferenceHandleStrategy implements IReferenceHandleStrategy {
    @Override
    public Reference handleReference(String quality) {
        Reference reference = new Reference();
        reference.setQuality("轻度污染");
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        pollutionEpisode.setAqi("101~150");
        pollutionEpisode.setSo2("501~650");
        pollutionEpisode.setNo2("201~700");
        pollutionEpisode.setPm10("151~250");
        pollutionEpisode.setCo("11~35");
        pollutionEpisode.setO3("201~300");
        pollutionEpisode.setO3Per8h("161~215");
        pollutionEpisode.setPm25("76~115");
        reference.setPollutionEpisode(pollutionEpisode);
        return reference;
    }
}
