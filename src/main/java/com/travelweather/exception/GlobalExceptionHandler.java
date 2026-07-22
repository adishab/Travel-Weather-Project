package com.travelweather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();

        // error messages
        error.put("error", "Your travel assistant hit a little turbulence: " + ex.getMessage());
        error.put("tip", "Try checking your destination name or adjusting your request!");
        error.put("status", "400 BAD REQUEST");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
