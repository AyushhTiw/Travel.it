package com.travelit.trip.service;

import com.travelit.trip.dto.CreateTripRequest;
import com.travelit.trip.dto.TripResponse;
import com.travelit.trip.dto.UpdateTripRequest;
import com.travelit.trip.entity.Trip;
import com.travelit.trip.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public TripResponse createTrip(CreateTripRequest request) {

        validateDates(
                request.getStartDate(),
                request.getEndDate()
        );

        Trip trip = new Trip(
                request.getTitle().trim(),
                clean(request.getDestination()),
                request.getStartDate(),
                request.getEndDate(),
                clean(request.getDescription())
        );

        Trip savedTrip = tripRepository.save(trip);

        return toResponse(savedTrip);
    }

    public TripResponse getTrip(Long id) {

        return toResponse(findTrip(id));
    }

    public List<TripResponse> getAllTrips() {

        return tripRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TripResponse> getActiveTrips() {

        return tripRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TripResponse> getTripsByDestination(
            String destination
    ) {

        return tripRepository
                .findByDestinationIgnoreCase(destination.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TripResponse updateTrip(
            Long id,
            UpdateTripRequest request
    ) {

        validateDates(
                request.getStartDate(),
                request.getEndDate()
        );

        Trip trip = findTrip(id);

        trip.setTitle(request.getTitle().trim());
        trip.setDestination(clean(request.getDestination()));
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setDescription(clean(request.getDescription()));

        Trip updatedTrip = tripRepository.save(trip);

        return toResponse(updatedTrip);
    }

    public void deleteTrip(Long id) {

        Trip trip = findTrip(id);

        tripRepository.delete(trip);
    }

    public void deactivateTrip(Long id) {

        Trip trip = findTrip(id);

        trip.setActive(false);

        tripRepository.save(trip);
    }

    private Trip findTrip(Long id) {

        return tripRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Trip not found"));
    }

    private void validateDates(
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        String cleaned = value.trim();

        return cleaned.isEmpty() ? null : cleaned;
    }

    private TripResponse toResponse(Trip trip) {

        return new TripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getDescription(),
                trip.isActive()
        );
    }
}