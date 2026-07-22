package com.travelweather.exception;

public class DestinationNotFoundException extends RuntimeException {
    public DestinationNotFoundException(String name) {
        super("Hmm… '" + name + "' isn’t on our travel map yet. Try exploring a different destination!");
    }
}
