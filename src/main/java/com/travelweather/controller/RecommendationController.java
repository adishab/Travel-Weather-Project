package com.travelweather.controller;

import com.travelweather.model.RecommendationResult;
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

    // GET — Recommendations with reasons + weather
    @GetMapping
    public List<RecommendationResult> getRecommendations(
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String budget,
            @RequestParam(required = false) List<String> activities
    ) {
        return recommendationService.getTopMatchesWithReasons(style, budget, activities, null);
    }
}
