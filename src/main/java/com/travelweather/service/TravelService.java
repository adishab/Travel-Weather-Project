package com.travelweather.service;

import org.springframework.stereotype.Service;
import com.travelweather.model.Destination;
import com.travelweather.model.Weather;
import com.travelweather.model.TravelDetails;
import com.travelweather.exception.DestinationNotFoundException;
import com.travelweather.exception.WeatherException;

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

    // GET — Good weather destinations (based on average temp)
    public List<Destination> getDestinationsWithGoodWeather() {
        List<Destination> all = getAllDestinations();
        List<Destination> good = new ArrayList<>();

        for (Destination d : all) {
            Weather w;
            try {
                w = weatherService.getWeatherForCity(d.getName());
            } catch (Exception e) {
                throw new WeatherException("Weather lookup failed for " + d.getName());
            }

            if (w.getTemperature() >= 65 && w.getTemperature() <= 85) {
                good.add(d);
            }
        }

        return good;
    }

    // GET — Destination details
    public TravelDetails getDetails(String city) {

        Destination destination = getAllDestinations()
                .stream()
                .filter(d -> d.getName().equalsIgnoreCase(city))
                .findFirst()
                .orElseThrow(() -> new DestinationNotFoundException(city));

        Weather weather;
        try {
            weather = weatherService.getWeatherForCity(city);
        } catch (Exception e) {
            throw new WeatherException("Weather lookup failed for " + city);
        }

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
