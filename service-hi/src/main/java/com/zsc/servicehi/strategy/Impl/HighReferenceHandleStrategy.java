package com.zsc.servicehi.strategy.Impl;

import com.zsc.servicehi.strategy.IReferenceHandleStrategy;
import model.pollutant.PollutionEpisode;
import model.weather.Reference;

public class HighReferenceHandleStrategy implements IReferenceHandleStrategy {

    @Override
    public Reference handleReference(String quality) {
        Reference reference = new Reference();
        reference.setQuality("优质");
        PollutionEpisode pollutionEpisode = new PollutionEpisode();
        pollutionEpisode.setAqi("0~50");
        pollutionEpisode.setSo2("0~150");
        pollutionEpisode.setNo2("0~100");
        pollutionEpisode.setPm10("0~50");
        pollutionEpisode.setCo("0~5");
        pollutionEpisode.setO3("0~160");
        pollutionEpisode.setO3Per8h("0~100");
        pollutionEpisode.setPm25("0~35");
        reference.setPollutionEpisode(pollutionEpisode);
        return reference;
    }

}