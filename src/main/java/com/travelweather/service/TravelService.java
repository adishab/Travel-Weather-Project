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

    // GET — All destinations
    public List<Destination> getAllDestinations() {
        return destinationDataService.getDestinations();
    }
    // GET — Recommendations
    public List<Destination> getRecommendedDestinations() {
        return destinationDataService.getDestinations();
    }

    // GET — Good weather destinations
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

    // GET — Destination details
    public TravelDetails getDetails(String city) {
        Destination destination = getRecommendedDestinations()
                .stream()
                .filter(d -> d.getName().equalsIgnoreCase(city))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        Weather weather = weatherService.getWeatherForCity(city);

        return new TravelDetails(destination, weather);
    }

    // POST — Add destination
    public Destination addDestination(Destination d) {
        return destinationDataService.addDestination(d);
    }

    // PUT — Update destination
    public Destination updateDestination(String name, Destination d) {
        return destinationDataService.updateDestination(name, d);
    }

    // DELETE — Remove destination
    public void deleteDestination(String name) {
        destinationDataService.deleteDestination(name);
    }
}


