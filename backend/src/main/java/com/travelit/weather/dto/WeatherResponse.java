package com.travelit.weather.dto;

import java.util.List;

public class WeatherResponse {

    private Double latitude;
    private Double longitude;
    private String timezone;

    private Double temperature;
    private Double apparentTemperature;
    private Integer relativeHumidity;
    private Double windSpeed;
    private Double precipitation;
    private Integer weatherCode;
    private String weatherDescription;

    private List<WeatherDayResponse> forecast;

    public WeatherResponse() {
    }

    public WeatherResponse(
            Double latitude,
            Double longitude,
            String timezone,
            Double temperature,
            Double apparentTemperature,
            Integer relativeHumidity,
            Double windSpeed,
            Double precipitation,
            Integer weatherCode,
            String weatherDescription,
            List<WeatherDayResponse> forecast
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.relativeHumidity = relativeHumidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.weatherCode = weatherCode;
        this.weatherDescription = weatherDescription;
        this.forecast = forecast;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public Integer getRelativeHumidity() {
        return relativeHumidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public List<WeatherDayResponse> getForecast() {
        return forecast;
    }
}