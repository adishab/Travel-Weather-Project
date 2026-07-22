package com.travelweather.service;

import com.travelweather.model.Destination;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final TravelService travelService;

    public RecommendationService(TravelService travelService) {
        this.travelService = travelService;
    }

    // Convert to low, medium, or high
    private String categorizeBudget(String budgetRange) {
        try {
            // Remove $ signs
            String cleaned = budgetRange.replace("$", "");

            // Split into min and max
            String[] parts = cleaned.split("-");

            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());

            // Categorize based on max value
            if (max < 1000) return "low";
            if (max <= 2000) return "medium";
            return "high";

        } catch (Exception e) {
            return "unknown"; // fallback if formatting is weird
        }
    }

    public List<Destination> getTopMatches(String style, String budget, List<String> activities) {

        // Get all destinations from Supabase
        List<Destination> all = travelService.getAllDestinations();

        Map<Destination, Integer> scores = new HashMap<>();

        for (Destination d : all) {
            int score = 0;

            // Travel Style Match
            if (style != null && d.getTravelStyle() != null &&
                    d.getTravelStyle().equalsIgnoreCase(style)) {
                score += 30;
            }

            // Budget Match (converted to low/medium/high)
            if (budget != null && d.getBudgetRange() != null) {
                String category = categorizeBudget(d.getBudgetRange());
                if (category.equalsIgnoreCase(budget)) {
                    score += 20;
                }
            }

            // Activity Match
            if (activities != null && d.getActivities() != null) {
                for (String act : activities) {
                    if (d.getActivities().contains(act)) {
                        score += 10;
                    }
                }
            }

            scores.put(d, score);
        }

        // Sort by score (highest first) and return top 3
        return scores.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

