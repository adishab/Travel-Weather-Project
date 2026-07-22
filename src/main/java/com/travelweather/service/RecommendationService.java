package com.travelweather.service;

import com.travelweather.model.Destination;
import com.travelweather.model.RecommendationResult;
import com.travelweather.model.TravelPersonalityRequest;
import com.travelweather.model.TravelPersonalityResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final TravelService travelService;
    private final WeatherService weatherService;

    public RecommendationService(TravelService travelService, WeatherService weatherService) {
        this.travelService = travelService;
        this.weatherService = weatherService;
    }

    // Convert to low, medium, or high
    private String categorizeBudget(String budgetRange) {
        try {
            String cleaned = budgetRange.replace("$", "");
            String[] parts = cleaned.split("-");
            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());

            if (max < 1000) return "low";
            if (max <= 2000) return "medium";
            return "high";

        } catch (Exception e) {
            return "unknown";
        }
    }

    // Partial Matching (Beach -> Beaches)
    private boolean partialMatch(String userActivity, String destinationActivity) {
        String u = userActivity.toLowerCase();
        String d = destinationActivity.toLowerCase();

        if (u.equals(d)) return true;
        if (d.contains(u) || u.contains(d)) return true;

        String uStem = u.replaceAll("(s|ing|ed)$", "");
        String dStem = d.replaceAll("(s|ing|ed)$", "");

        return uStem.equals(dStem);
    }

    // Synonym Matching
    private boolean synonymMatch(String userActivity, String destinationActivity) {
        Map<String, List<String>> synonyms = Map.of(
                "beach", List.of("beaches", "coast", "shore"),
                "hike", List.of("hiking", "trail", "trek"),
                "food", List.of("food tours", "cuisine", "dining"),
                "museum", List.of("museums", "gallery"),
                "shopping", List.of("markets", "shops"),
                "nightlife", List.of("clubs", "bars", "party"),
                "temple", List.of("temples", "shrines")
        );

        String u = userActivity.toLowerCase();
        String d = destinationActivity.toLowerCase();

        if (synonyms.containsKey(u)) {
            for (String syn : synonyms.get(u)) {
                if (d.contains(syn)) return true;
            }
        }

        return false;
    }

    // Budget Closeness Scoring
    private int budgetScore(String budgetRange, int userBudget) {
        try {
            String[] parts = budgetRange.replace("$", "").split("-");
            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());

            if (userBudget >= min && userBudget <= max) {
                return 10;
            }

            int distance = Math.min(Math.abs(userBudget - min), Math.abs(userBudget - max));

            if (distance < 300) return 8;
            if (distance < 600) return 5;
            if (distance < 1000) return 2;

        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    //  WEATHER SCORING
    private int weatherScore(String cityName, List<String> reasons) {
        try {
            var weather = weatherService.getWeatherForCity(cityName);

            String condition = weather.getCondition().toLowerCase();
            double temp = weather.getTemperature();

            int score = 0;

            if (condition.contains("sun")) {
                score += 8;
                reasons.add("Weather is sunny—great for outdoor activities.");
            } else if (condition.contains("cloud")) {
                score += 4;
                reasons.add("Weather is cloudy but still good for exploring.");
            } else if (condition.contains("rain")) {
                score -= 5;
                reasons.add("Rainy today—some outdoor plans might be affected.");
            }

            if (temp >= 75 && temp <= 90) {
                score += 5;
                reasons.add("Temperature is warm and comfortable.");
            } else if (temp >= 60 && temp < 75) {
                score += 3;
                reasons.add("Temperature is mild—great for walking and sightseeing.");
            } else if (temp < 50) {
                score -= 3;
                reasons.add("Quite cold today—pack warm clothes.");
            }

            return score;

        } catch (Exception e) {
            return 0;
        }
    }

    //  MAIN RECOMMENDATION METHOD WITH REASONS + WEATHER
    public List<RecommendationResult> getTopMatchesWithReasons(
            String style,
            String budget,
            List<String> activities,
            String weatherPreference
    ) {

        List<Destination> all = travelService.getAllDestinations();
        Map<Destination, Integer> scores = new HashMap<>();
        Map<Destination, List<String>> reasonsMap = new HashMap<>();

        Integer userBudgetNumber = null;
        if (budget != null) {
            try {
                userBudgetNumber = Integer.parseInt(budget);
            } catch (Exception ignored) {}
        }

        for (Destination d : all) {
            int score = 0;
            List<String> reasons = new ArrayList<>();

            // Travel Style Match
            if (style != null && d.getTravelStyle() != null &&
                    d.getTravelStyle().equalsIgnoreCase(style)) {
                score += 30;
                reasons.add("Matches your preferred travel style: " + style);
            }

            // Budget Category Match
            if (budget != null && d.getBudgetRange() != null) {
                String category = categorizeBudget(d.getBudgetRange());
                if (category.equalsIgnoreCase(budget)) {
                    score += 20;
                    reasons.add("Fits your budget category: " + budget);
                }
            }

            // Budget Closeness
            if (userBudgetNumber != null && d.getBudgetRange() != null) {
                int bScore = budgetScore(d.getBudgetRange(), userBudgetNumber);
                if (bScore > 0) {
                    score += bScore;
                    reasons.add("Budget is close to your target.");
                }
            }

            // Activity Matching
            if (activities != null && d.getActivities() != null) {
                int activityScore = 0;
                for (String act : activities) {
                    for (String destAct : d.getActivities()) {
                        if (partialMatch(act, destAct) || synonymMatch(act, destAct)) {
                            activityScore += 10;
                        }
                    }
                }
                if (activityScore > 0) {
                    score += activityScore;
                    reasons.add("Matches your preferred activities.");
                }
            }

            // WEATHER SCORING
            int wScore = weatherScore(d.getName(), reasons);
            score += wScore;

            // WEATHER FILTERING
            if (weatherPreference != null) {
                if (weatherPreference.equalsIgnoreCase("warm") && d.getAverageTemperature() < 60) {
                    score -= 10;
                    reasons.add("Average temperature is cooler than your warm preference.");
                } else if (weatherPreference.equalsIgnoreCase("cold") && d.getAverageTemperature() > 75) {
                    score -= 10;
                    reasons.add("Average temperature is warmer than your cold preference.");
                }
            }

            scores.put(d, score);
            reasonsMap.put(d, reasons);
        }

        return scores.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(entry -> new RecommendationResult(entry.getKey(), reasonsMap.get(entry.getKey())))
                .collect(Collectors.toList());
    }

    // SURPRISE ME SUPPORT
    public RecommendationResult surpriseMe(String style, String budget, List<String> activities, String weather) {
        List<RecommendationResult> results = getTopMatchesWithReasons(style, budget, activities, weather);

        if (results.isEmpty()) return null;

        Random rand = new Random();
        return results.get(rand.nextInt(results.size()));
    }

    // PERSONALITY QUIZ SUPPORT
    public TravelPersonalityResult evaluatePersonality(TravelPersonalityRequest req) {

        String personality;
        String style;

        if ("nature".equalsIgnoreCase(req.getPrefersNatureOrCity()) &&
                "adventure".equalsIgnoreCase(req.getPrefersRelaxOrAdventure())) {
            personality = "The Adventurer";
            style = "Adventure";
        } else if ("nature".equalsIgnoreCase(req.getPrefersNatureOrCity()) &&
                "relax".equalsIgnoreCase(req.getPrefersRelaxOrAdventure())) {
            personality = "The Relaxer";
            style = "Relaxing";
        } else if ("city".equalsIgnoreCase(req.getPrefersNatureOrCity()) &&
                "relax".equalsIgnoreCase(req.getPrefersRelaxOrAdventure())) {
            personality = "The Urban Explorer";
            style = "Urban";
        } else if ("city".equalsIgnoreCase(req.getPrefersNatureOrCity()) &&
                "adventure".equalsIgnoreCase(req.getPrefersRelaxOrAdventure())) {
            personality = "The Culture Seeker";
            style = "Cultural";
        } else {
            personality = "The Wanderer";
            style = "Scenic";
        }

        return new TravelPersonalityResult(personality, style);
    }
}


