package com.travelweather.model;

public class TravelPersonalityResult {

    private String personality;
    private String recommendedStyle;

    public TravelPersonalityResult(String personality, String recommendedStyle) {
        this.personality = personality;
        this.recommendedStyle = recommendedStyle;
    }

    public String getPersonality() {
        return personality;
    }

    public String getRecommendedStyle() {
        return recommendedStyle;
    }
}
