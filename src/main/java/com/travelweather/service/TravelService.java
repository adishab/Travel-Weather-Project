package com.travelweather.service;

import org.springframework.stereotype.Service;
import com.travelweather.model.Destination;
import com.travelweather.model.Weather;
import com.travelweather.model.TravelDetails;

import java.util.ArrayList;
import java.util.List;

@Service
public class TravelService {

    private final WeatherService weatherService;
    private final DestinationDataService destinationDataService;
    public TravelService(WeatherService weatherService, DestinationDataService destinationDataService) {

        this.weatherService = weatherService;
        this.destinationDataService = destinationDataService;
    }

    public List<Destination> getRecommendedDestinations(){
        return destinationDataService.getDestinations();
    }

    public List<Destination> getDestinationsWithGoodWeather() {
        List<Destination> all = getRecommendedDestinations();
        List<Destination> good = new ArrayList<>();

        for (Destination d : all) {
            Weather w = weatherService.getWeatherForCity(d.getName());

            if (w.getTemperature() >= 65 && w.getTemperature() <= 85) {
                good.add(d);
            }
        }

        return good;
    }

    public TravelDetails getDetails(String city) {
        Destination destination = getRecommendedDestinations()
                .stream()
                .filter(d -> d.getName().equalsIgnoreCase(city))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        Weather weather = weatherService.getWeatherForCity(city);

        return new TravelDetails(destination, weather);
    }
}

