package com.travelit.weather.controller;

import com.travelit.weather.dto.WeatherResponse;
import com.travelit.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam double latitude,
            @RequestParam double longitude
    ) {
        return ResponseEntity.ok(
                weatherService.getWeather(latitude, longitude)
        );
    }
}