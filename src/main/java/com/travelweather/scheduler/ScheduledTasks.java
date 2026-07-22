package com.travelweather.scheduler;

import com.travelweather.model.Destination;
import com.travelweather.service.TravelService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Random;

@Component
public class ScheduledTasks {

    private final TravelService travelService;
    private final Random random = new Random();

    public ScheduledTasks(TravelService travelService) {
        this.travelService = travelService;
    }

    // Task #1 — Log weather for a random destination every min
    @Scheduled(fixedRate = 60000)
    public void logRandomDestinationWeather() {
        List<Destination> destinations = travelService.getAllDestinations();

        if (destinations.isEmpty()) {
            System.out.println("[Scheduled Task] No destinations found.");
            return;
        }

        Destination randomDestination = destinations.get(random.nextInt(destinations.size()));

        System.out.println("[Scheduled Task] Checking weather for: " + randomDestination.getName());

        try {
            var details = travelService.getDetails(randomDestination.getName());
            System.out.println("[Scheduled Task] Weather in " + randomDestination.getName() +
                    ": " + details.getWeather().getCondition());
        } catch (Exception e) {
            System.out.println("[Scheduled Task] Failed to fetch weather: " + e.getMessage());
        }
    }

    // Task #2 — Insert a randomized travel tip every 3 min
    @Scheduled(fixedRate = 180000)
    public void insertTravelTip() {

        // Randomized travel tip content
        String[] descriptions = {
                "Remember to check the weather before traveling!",
                "Always pack a small first-aid kit for emergencies.",
                "Keep digital copies of your passport and travel documents.",
                "Try local foods — it's the best way to experience culture!",
                "Stay hydrated and take breaks during long sightseeing days.",
                "Learn a few basic phrases of the local language.",
                "Avoid carrying all your cash in one place.",
                "Wear comfortable shoes for long walking days.",
                "Check local holidays — attractions may be closed.",
                "Keep your phone charged and carry a portable battery."
        };

        String[] activities = {
                "Planning",
                "Packing",
                "Safety",
                "Food",
                "Relaxation",
                "Preparation",
                "Navigation"
        };

        String[] itineraries = {
                "Check weather",
                "Pack essentials",
                "Try local cuisine",
                "Stay safe",
                "Take breaks",
                "Learn local phrases",
                "Charge devices",
                "Review travel documents"
        };

        // Create randomized tip
        Destination tip = new Destination();
        tip.setName("Travel Tip " + System.currentTimeMillis());
        tip.setCountry("N/A");
        tip.setDescription(descriptions[random.nextInt(descriptions.length)]);
        tip.setAverageTemperature(0);
        tip.setBudgetRange("N/A");
        tip.setActivities(List.of(activities[random.nextInt(activities.length)]));
        tip.setTravelStyle("General");
        tip.setItinerary(List.of(itineraries[random.nextInt(itineraries.length)]));

        travelService.addDestination(tip);

        // Clean up old travel tips, keep only the newest 3
        List<Destination> all = travelService.getAllDestinations();

        List<Destination> tips = all.stream()
                .filter(d -> d.getName() != null && d.getName().startsWith("Travel Tip"))
                .sorted((a, b) -> b.getName().compareTo(a.getName())) // newest first
                .collect(Collectors.toList());

        if (tips.size() > 3) {
            List<Destination> oldTips = tips.subList(3, tips.size());
            for (Destination old : oldTips) {
                travelService.deleteDestination(old.getName());
            }
        }

        System.out.println("[Scheduled Task] Inserted randomized travel tip.");
    }
}
