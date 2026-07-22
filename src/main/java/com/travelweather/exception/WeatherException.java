package com.travelweather.exception;

public class WeatherException extends RuntimeException {
    public WeatherException(String message) {
        super("The weather service is having a little turbulence. Please try again soon!");
    }
}
