package com.travelit.explore.service;

import com.travelit.explore.dto.NearbyPlaceResponse;
import com.travelit.place.dto.PlaceResponse;
import com.travelit.place.service.PlaceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class ExploreService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final PlaceService placeService;

    public ExploreService(PlaceService placeService) {
        this.placeService = placeService;
    }

    public List<NearbyPlaceResponse> findNearbyPlaces(
            double latitude,
            double longitude,
            double radiusKm
    ) {

        validateCoordinates(latitude, longitude);

        if (radiusKm <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }

        List<PlaceResponse> places = placeService.getActivePlaces();

        return places.stream()
                .filter(place ->
                        place.getLatitude() != null
                                && place.getLongitude() != null
                )
                .map(place -> {
                    double distance = calculateDistance(
                            latitude,
                            longitude,
                            place.getLatitude(),
                            place.getLongitude()
                    );

                    return new NearbyPlaceResponse(
                            place.getId(),
                            place.getName(),
                            place.getDescription(),
                            place.getCity(),
                            place.getState(),
                            place.getCountry(),
                            place.getLatitude(),
                            place.getLongitude(),
                            place.getAddress(),
                            distance
                    );
                })
                .filter(place -> place.getDistanceKm() <= radiusKm)
                .sorted(Comparator.comparingDouble(NearbyPlaceResponse::getDistanceKm))
                .toList();
    }

    private double calculateDistance(
            double latitude1,
            double longitude1,
            BigDecimal latitude2,
            BigDecimal longitude2
    ) {

        double lat1Radians = Math.toRadians(latitude1);
        double lon1Radians = Math.toRadians(longitude1);

        double lat2Radians = Math.toRadians(latitude2.doubleValue());
        double lon2Radians = Math.toRadians(longitude2.doubleValue());

        double deltaLatitude = lat2Radians - lat1Radians;
        double deltaLongitude = lon2Radians - lon1Radians;

        double a =
                Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                        + Math.cos(lat1Radians)
                        * Math.cos(lat2Radians)
                        * Math.sin(deltaLongitude / 2)
                        * Math.sin(deltaLongitude / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(EARTH_RADIUS_KM * c * 100.0) / 100.0;
    }

    private void validateCoordinates(double latitude, double longitude) {

        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90"
            );
        }

        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180"
            );
        }
    }
}