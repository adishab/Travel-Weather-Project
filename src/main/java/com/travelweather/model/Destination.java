package com.travelweather.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Destination {

    private String name;
    private String country;
    private String description;

    @JsonProperty("average_temperature")
    private double averageTemperature;

    @JsonProperty("budget_range")
    private String budgetRange;

    private List<String> activities;

    @JsonProperty("travel_style")
    private String travelStyle;

    private List<String> itinerary;

    // ⭐ No-argument constructor (required for scheduled tasks & JSON)
    public Destination() {
    }

    // ⭐ Full constructor
    public Destination(
            @JsonProperty("name") String name,
            @JsonProperty("country") String country,
            @JsonProperty("description") String description,
            @JsonProperty("average_temperature") double averageTemperature,
            @JsonProperty("budget_range") String budgetRange,
            @JsonProperty("activities") List<String> activities,
            @JsonProperty("travel_style") String travelStyle,
            @JsonProperty("itinerary") List<String> itinerary
    ) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.averageTemperature = averageTemperature;
        this.budgetRange = budgetRange;
        this.activities = activities;
        this.travelStyle = travelStyle;
        this.itinerary = itinerary;
    }

    // ⭐ Getters
    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public String getBudgetRange() {
        return budgetRange;
    }

    public List<String> getActivities() {
        return activities;
    }

    public String getTravelStyle() {
        return travelStyle;
    }

    public List<String> getItinerary() {
        return itinerary;
    }

    // ⭐ Setters (these FIX your scheduled task errors)
    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAverageTemperature(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public void setBudgetRange(String budgetRange) {
        this.budgetRange = budgetRange;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public void setTravelStyle(String travelStyle) {
        this.travelStyle = travelStyle;
    }

    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary;
    }
}
