package com.travelit.weather.dto;

public class WeatherDayResponse {

    private String date;
    private Integer weatherCode;
    private String weatherDescription;
    private Double minTemperature;
    private Double maxTemperature;
    private Integer precipitationProbability;
    private Double precipitation;
    private String sunrise;
    private String sunset;

    public WeatherDayResponse() {
    }

    public WeatherDayResponse(
            String date,
            Integer weatherCode,
            String weatherDescription,
            Double minTemperature,
            Double maxTemperature,
            Integer precipitationProbability,
            Double precipitation,
            String sunrise,
            String sunset
    ) {
        this.date = date;
        this.weatherCode = weatherCode;
        this.weatherDescription = weatherDescription;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.precipitationProbability = precipitationProbability;
        this.precipitation = precipitation;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getDate() {
        return date;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public Integer getPrecipitationProbability() {
        return precipitationProbability;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}