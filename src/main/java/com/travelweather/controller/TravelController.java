package com.travelweather.controller;

import com.travelweather.model.Destination;
import com.travelweather.model.TravelDetails;
import com.travelweather.service.TravelService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    // GET — All destinations
    @GetMapping("/travel")
    public List<Destination> getAllDestinations() {
        return travelService.getAllDestinations();
    }
    // GET — Recommendations
    @GetMapping("/travel/recommendations")
    public List<Destination> getRecommendations() {
        return travelService.getRecommendedDestinations();
    }

    // GET — Good weather destinations
    @GetMapping("/travel/good-weather")
    public List<Destination> getGoodWeatherDestinations() {
        return travelService.getDestinationsWithGoodWeather();
    }

    // GET — Destination details
    @GetMapping("/travel/details")
    public TravelDetails getDetails(@RequestParam("city") String city) {
        return travelService.getDetails(city);
    }

    // POST — Add a new destination
    @PostMapping("/travel")
    public Destination addDestination(@RequestBody Destination d) {
        return travelService.addDestination(d);
    }

    // PUT — Update a destination
    @PutMapping("/travel/{name}")
    public Destination updateDestination(@PathVariable("name") String name, @RequestBody Destination d) {
        return travelService.updateDestination(name, d);
    }

    // DELETE — Remove a destination
    @DeleteMapping("/travel/{name}")
    public String deleteDestination(@PathVariable("name") String name) {
        travelService.deleteDestination(name);
        return "Destination deleted: " + name;
    }
}
