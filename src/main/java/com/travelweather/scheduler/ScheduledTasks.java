package com.travelweather.scheduler;

import com.travelweather.model.Destination;
import com.travelweather.service.TravelService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    // Task #2 — Insert a travel tip every 2 min
    @Scheduled(fixedRate = 120000)
    public void insertTravelTip() {
        Destination tip = new Destination();
        tip.setName("Travel Tip " + System.currentTimeMillis());
        tip.setCountry("N/A");
        tip.setDescription("Remember to check the weather before traveling!");
        tip.setAverageTemperature(0);
        tip.setBudgetRange("N/A");
        tip.setActivities(List.of("Planning"));
        tip.setTravelStyle("General");
        tip.setItinerary(List.of("Check weather", "Pack accordingly"));

        travelService.addDestination(tip);

        System.out.println("[Scheduled Task] Inserted new travel tip into database.");
    }
}
