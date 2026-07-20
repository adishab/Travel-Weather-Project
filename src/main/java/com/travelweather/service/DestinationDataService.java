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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class DestinationDataService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    // GET — Fetch all destinations
    public List<Destination> getDestinations() {
        List<Destination> destinations = new ArrayList<>();

        try {
            String url = supabaseUrl + "/rest/v1/destination?select=*";

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);

            HttpEntity<String> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

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

                for (JsonNode activity : record.get("activities")) {
                    activities.add(activity.asText());
                }

                for (JsonNode day : record.get("itinerary")) {
                    itinerary.add(day.asText());
                }

                destinations.add(new Destination(
                        record.get("name").asText(),
                        record.get("country").asText(),
                        record.get("description").asText(),
                        record.get("average_temperature").asDouble(),
                        record.get("budget_range").asText(),
                        activities,
                        record.get("travel_style").asText(),
                        itinerary
                ));
            }

            return destinations;

        } catch (Exception e) {
            throw new RuntimeException("Could not load destinations from Supabase: " + e.getMessage());
        }
    }

    // POST — Add a new destination
    public Destination addDestination(Destination destination) {
        try {
            String url = supabaseUrl + "/rest/v1/destination";

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("Content-Type", "application/json");
            headers.set("Prefer", "return=representation");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(destination);

            HttpEntity<String> request = new HttpEntity<>(json, headers);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return destination;

        } catch (Exception e) {
            throw new RuntimeException("Could not add destination: " + e.getMessage());
        }
    }

    // PUT — Update a destination
    public Destination updateDestination(String name, Destination updated) {
        try {
            String url = supabaseUrl + "/rest/v1/destination?name=eq." + name;

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("Content-Type", "application/json");
            headers.set("Prefer", "return=representation");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(updated);

            HttpEntity<String> request = new HttpEntity<>(json, headers);
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

            restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    request,
                    String.class
            );

            return updated;

        } catch (Exception e) {
            throw new RuntimeException("Could not update destination: " + e.getMessage());
        }
    }

    // DELETE — Remove a destination
    public void deleteDestination(String name) {
        try {
            String url = supabaseUrl + "/rest/v1/destination?name=eq." + name;

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);

            HttpEntity<String> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    String.class
            );

        } catch (Exception e) {
            throw new RuntimeException("Sorry! Could not delete destination: " + e.getMessage());
        }
    }
}