package com.travelit.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelit.weather.dto.WeatherDayResponse;
import com.travelit.weather.dto.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WeatherService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.open-meteo.com")
                .build();
    }

    public WeatherResponse getWeather(
            double latitude,
            double longitude
    ) {

        String response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam(
                                "current",
                                "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m"
                        )
                        .queryParam(
                                "daily",
                                "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max,precipitation_sum,sunrise,sunset"
                        )
                        .queryParam("forecast_days", 7)
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(String.class);

        try {

            JsonNode root = objectMapper.readTree(response);

            JsonNode current = root.path("current");
            JsonNode daily = root.path("daily");

            Integer currentWeatherCode =
                    current.path("weather_code").isNumber()
                            ? current.path("weather_code").asInt()
                            : null;

            List<WeatherDayResponse> forecast = new ArrayList<>();

            JsonNode dates = daily.path("time");
            JsonNode weatherCodes = daily.path("weather_code");
            JsonNode maxTemps = daily.path("temperature_2m_max");
            JsonNode minTemps = daily.path("temperature_2m_min");
            JsonNode precipitationProbability =
                    daily.path("precipitation_probability_max");
            JsonNode precipitation =
                    daily.path("precipitation_sum");
            JsonNode sunrise = daily.path("sunrise");
            JsonNode sunset = daily.path("sunset");

            if (dates.isArray()) {

                for (int i = 0; i < dates.size(); i++) {

                    Integer code =
                            weatherCodes.path(i).isNumber()
                                    ? weatherCodes.path(i).asInt()
                                    : null;

                    WeatherDayResponse day =
                            new WeatherDayResponse(
                                    dates.path(i).asText(null),
                                    code,
                                    getWeatherDescription(code),
                                    minTemps.path(i).isNumber()
                                            ? minTemps.path(i).asDouble()
                                            : null,
                                    maxTemps.path(i).isNumber()
                                            ? maxTemps.path(i).asDouble()
                                            : null,
                                    precipitationProbability.path(i).isNumber()
                                            ? precipitationProbability.path(i).asInt()
                                            : null,
                                    precipitation.path(i).isNumber()
                                            ? precipitation.path(i).asDouble()
                                            : null,
                                    sunrise.path(i).asText(null),
                                    sunset.path(i).asText(null)
                            );

                    forecast.add(day);
                }
            }

            return new WeatherResponse(
                    root.path("latitude").asDouble(),
                    root.path("longitude").asDouble(),
                    root.path("timezone").asText(null),

                    current.path("temperature_2m").isNumber()
                            ? current.path("temperature_2m").asDouble()
                            : null,

                    current.path("apparent_temperature").isNumber()
                            ? current.path("apparent_temperature").asDouble()
                            : null,

                    current.path("relative_humidity_2m").isNumber()
                            ? current.path("relative_humidity_2m").asInt()
                            : null,

                    current.path("wind_speed_10m").isNumber()
                            ? current.path("wind_speed_10m").asDouble()
                            : null,

                    current.path("precipitation").isNumber()
                            ? current.path("precipitation").asDouble()
                            : null,

                    currentWeatherCode,
                    getWeatherDescription(currentWeatherCode),

                    forecast
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Open-Meteo weather response",
                    e
            );
        }
    }

    private String getWeatherDescription(Integer code) {

        if (code == null) {
            return "Unknown";
        }

        return switch (code) {
            case 0 -> "Clear sky";

            case 1, 2, 3 ->
                    "Mainly clear, partly cloudy or overcast";

            case 45, 48 ->
                    "Fog";

            case 51, 53, 55 ->
                    "Drizzle";

            case 56, 57 ->
                    "Freezing drizzle";

            case 61, 63, 65 ->
                    "Rain";

            case 66, 67 ->
                    "Freezing rain";

            case 71, 73, 75 ->
                    "Snow";

            case 77 ->
                    "Snow grains";

            case 80, 81, 82 ->
                    "Rain showers";

            case 85, 86 ->
                    "Snow showers";

            case 95 ->
                    "Thunderstorm";

            case 96, 99 ->
                    "Thunderstorm with hail";

            default ->
                    "Unknown";
        };
    }
}