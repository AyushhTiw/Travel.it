package com.travelit.trip.service;

import com.travelit.trip.dto.CreateItineraryVersionRequest;
import com.travelit.trip.dto.ItineraryVersionResponse;
import com.travelit.trip.entity.ItineraryVersion;
import com.travelit.trip.entity.Trip;
import com.travelit.trip.repository.ItineraryVersionRepository;
import com.travelit.trip.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryVersionService {

    private final ItineraryVersionRepository versionRepository;
    private final TripRepository tripRepository;

    public ItineraryVersionService(
            ItineraryVersionRepository versionRepository,
            TripRepository tripRepository
    ) {
        this.versionRepository = versionRepository;
        this.tripRepository = tripRepository;
    }

    public ItineraryVersionResponse createVersion(
            Long tripId,
            CreateItineraryVersionRequest request
    ) {

        if (request.getVersionNumber() <= 0) {
            throw new IllegalArgumentException(
                    "Version number must be greater than 0"
            );
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Trip not found"));

        if (versionRepository.existsByTripIdAndVersionNumber(
                tripId,
                request.getVersionNumber()
        )) {
            throw new IllegalArgumentException(
                    "Version number already exists for this trip"
            );
        }

        ItineraryVersion version = new ItineraryVersion(
                trip,
                request.getVersionNumber(),
                clean(request.getDescription())
        );

        return toResponse(
                versionRepository.save(version)
        );
    }

    public List<ItineraryVersionResponse> getVersions(
            Long tripId
    ) {

        if (!tripRepository.existsById(tripId)) {
            throw new IllegalArgumentException("Trip not found");
        }

        return versionRepository
                .findByTripIdOrderByVersionNumberDesc(tripId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ItineraryVersionResponse getVersion(
            Long tripId,
            Long versionId
    ) {

        ItineraryVersion version = versionRepository
                .findById(versionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Itinerary version not found"
                        ));

        if (!version.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException(
                    "Itinerary version does not belong to this trip"
            );
        }

        return toResponse(version);
    }

    public void deleteVersion(
            Long tripId,
            Long versionId
    ) {

        ItineraryVersion version = versionRepository
                .findById(versionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Itinerary version not found"
                        ));

        if (!version.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException(
                    "Itinerary version does not belong to this trip"
            );
        }

        versionRepository.delete(version);
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        String cleaned = value.trim();

        return cleaned.isEmpty() ? null : cleaned;
    }

    private ItineraryVersionResponse toResponse(
            ItineraryVersion version
    ) {

        return new ItineraryVersionResponse(
                version.getId(),
                version.getTrip().getId(),
                version.getVersionNumber(),
                version.getCreatedAt(),
                version.getDescription()
        );
    }
}