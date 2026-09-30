package com.travelit.place.dto;

public class NearbyPlaceForAi {
    private String placeId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
    private Double rating;
    private String primaryType;
    private String googleMapsUrl;

    public NearbyPlaceForAi() {
    }

    public NearbyPlaceForAi(
            String placeId,
            String name,
            String address,
            Double latitude,
            Double longitude,
            Double distanceKm,
            Double rating,
            String primaryType,
            String googleMapsUrl
    ) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
        this.rating = rating;
        this.primaryType = primaryType;
        this.googleMapsUrl = googleMapsUrl;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl;
    }
}
