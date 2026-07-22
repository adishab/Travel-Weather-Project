package com.travelweather.model;

public class TravelPersonalityRequest {

    private String prefersNatureOrCity;        // "nature" or "city"
    private String prefersRelaxOrAdventure;    // "relax" or "adventure"
    private String prefersWarmOrCold;          // "warm" or "cold"
    private String likesNightlife;             // "yes" or "no"
    private String likesFood;                  // "yes" or "no"

    public String getPrefersNatureOrCity() {
        return prefersNatureOrCity;
    }

    public void setPrefersNatureOrCity(String prefersNatureOrCity) {
        this.prefersNatureOrCity = prefersNatureOrCity;
    }

    public String getPrefersRelaxOrAdventure() {
        return prefersRelaxOrAdventure;
    }

    public void setPrefersRelaxOrAdventure(String prefersRelaxOrAdventure) {
        this.prefersRelaxOrAdventure = prefersRelaxOrAdventure;
    }

    public String getPrefersWarmOrCold() {
        return prefersWarmOrCold;
    }

    public void setPrefersWarmOrCold(String prefersWarmOrCold) {
        this.prefersWarmOrCold = prefersWarmOrCold;
    }

    public String getLikesNightlife() {
        return likesNightlife;
    }

    public void setLikesNightlife(String likesNightlife) {
        this.likesNightlife = likesNightlife;
    }

    public String getLikesFood() {
        return likesFood;
    }

    public void setLikesFood(String likesFood) {
        this.likesFood = likesFood;
    }
}
