package com.travelweather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TravelWeatherProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelWeatherProjectApplication.class, args);
    }
}