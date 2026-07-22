package com.travelweather.controller;

import com.travelweather.model.Destination;
import com.travelweather.model.TravelDetails;
import com.travelweather.service.TravelService;
import com.travelweather.model.TravelPersonalityRequest;
import com.travelweather.model.TravelPersonalityResult;
import com.travelweather.service.RecommendationService;
import com.travelweather.model.RecommendationResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class TravelController {

    private final TravelService travelService;

    // Inject RecommendationService
    private final RecommendationService recommendationService;

    // Spring can inject both services
    public TravelController(TravelService travelService, RecommendationService recommendationService) {
        this.travelService = travelService;
        this.recommendationService = recommendationService;
    }

    // GET — All destinations
    @GetMapping("/travel")
    public List<Destination> getAllDestinations() {
        return travelService.getAllDestinations();
    }


    // GET — Weather only recommendations
    @GetMapping("/recommend/weather")
    public List<RecommendationResult> recommendByWeather(
            @RequestParam String weather
    ) {
        return recommendationService.getTopMatchesWithReasons(null, null, null, weather);
    }

    // GET — Surprise Me
    @GetMapping("/recommend/surprise")
    public RecommendationResult surprise(
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String budget,
            @RequestParam(required = false) List<String> activities,
            @RequestParam(required = false) String weather
    ) {
        List<RecommendationResult> results =
                recommendationService.getTopMatchesWithReasons(style, budget, activities, weather);

        if (results.isEmpty()) {
            return null;
        }

        Random rand = new Random();
        return results.get(rand.nextInt(results.size()));
    }

    // GET — Good weather destinations (your original endpoint)
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

    // POST — Personality quiz
    @PostMapping("/personality-quiz")
    public TravelPersonalityResult personalityQuiz(@RequestBody TravelPersonalityRequest req) {
        return recommendationService.evaluatePersonality(req);
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
