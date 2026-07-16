package com.travelweather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelweather.model.Destination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DestinationDataService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    public List<Destination> getDestinations() {
        List<Destination> destinations = new ArrayList<>();

        try {
            String url =
                    supabaseUrl + "/rest/v1/destination?select=*";

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);

            HttpEntity<String> request =
                    new HttpEntity<>(headers);

            RestTemplate restTemplate =
                    new RestTemplate();

            String response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class
            ).getBody();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode records = mapper.readTree(response);

            for (JsonNode record : records) {
                List<String> activities = new ArrayList<>();
                List<String> itinerary = new ArrayList<>();

                for (JsonNode activity :
                        record.get("activities")) {
                    activities.add(activity.asText());
                }

                for (JsonNode day :
                        record.get("itinerary")) {
                    itinerary.add(day.asText());
                }

                destinations.add(new Destination(
                        record.get("name").asText(),
                        record.get("country").asText(),
                        record.get("description").asText(),
                        record.get("average_temperature")
                                .asDouble(),
                        record.get("budget_range").asText(),
                        activities,
                        record.get("travel_style").asText(),
                        itinerary
                ));
            }

            return destinations;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not load destinations from Supabase: "
                            + e.getMessage()
            );
        }
    }
}