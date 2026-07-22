package com.travelweather.controller;

import com.travelweather.model.Destination;
import com.travelweather.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public List<Destination> getRecommendations(
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String budget,
            @RequestParam(required = false) List<String> activities
    ) {
        return recommendationService.getTopMatches(style, budget, activities);
    }
}
