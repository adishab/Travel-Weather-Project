package com.travelweather.model;

import java.util.List;

public class RecommendationResult {

    private Destination destination;
    private List<String> reasons;

    public RecommendationResult(Destination destination, List<String> reasons) {
        this.destination = destination;
        this.reasons = reasons;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}
