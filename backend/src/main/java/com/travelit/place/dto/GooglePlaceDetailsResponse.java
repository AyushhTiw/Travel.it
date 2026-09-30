package com.travelit.place.dto;

import java.util.List;

public class GooglePlaceDetailsResponse {

    private String placeId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private String primaryType;

    private String nationalPhoneNumber;
    private String internationalPhoneNumber;
    private String websiteUri;
    private String googleMapsUri;
    private String businessStatus;

    private Boolean openNow;
    private List<String> weekdayDescriptions;

    public GooglePlaceDetailsResponse() {
    }

    public GooglePlaceDetailsResponse(
            String placeId,
            String name,
            String address,
            Double latitude,
            Double longitude,
            Double rating,
            String primaryType,
            String nationalPhoneNumber,
            String internationalPhoneNumber,
            String websiteUri,
            String googleMapsUri,
            String businessStatus,
            Boolean openNow,
            List<String> weekdayDescriptions
    ) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.primaryType = primaryType;
        this.nationalPhoneNumber = nationalPhoneNumber;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.websiteUri = websiteUri;
        this.googleMapsUri = googleMapsUri;
        this.businessStatus = businessStatus;
        this.openNow = openNow;
        this.weekdayDescriptions = weekdayDescriptions;
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

    public String getNationalPhoneNumber() {
        return nationalPhoneNumber;
    }

    public void setNationalPhoneNumber(String nationalPhoneNumber) {
        this.nationalPhoneNumber = nationalPhoneNumber;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(String websiteUri) {
        this.websiteUri = websiteUri;
    }

    public String getGoogleMapsUri() {
        return googleMapsUri;
    }

    public void setGoogleMapsUri(String googleMapsUri) {
        this.googleMapsUri = googleMapsUri;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<String> getWeekdayDescriptions() {
        return weekdayDescriptions;
    }

    public void setWeekdayDescriptions(List<String> weekdayDescriptions) {
        this.weekdayDescriptions = weekdayDescriptions;
    }
}