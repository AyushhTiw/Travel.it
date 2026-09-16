package com.travelit.trip.service;

import com.travelit.trip.dto.CreateTripDayRequest;
import com.travelit.trip.dto.TripDayResponse;
import com.travelit.trip.dto.UpdateTripDayRequest;
import com.travelit.trip.entity.Trip;
import com.travelit.trip.entity.TripDay;
import com.travelit.trip.repository.TripDayRepository;
import com.travelit.trip.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripDayService {

    private final TripDayRepository tripDayRepository;
    private final TripRepository tripRepository;

    public TripDayService(
            TripDayRepository tripDayRepository,
            TripRepository tripRepository
    ) {
        this.tripDayRepository = tripDayRepository;
        this.tripRepository = tripRepository;
    }

    public TripDayResponse createTripDay(
            Long tripId,
            CreateTripDayRequest request
    ) {

        Trip trip = findTrip(tripId);

        validateDayNumber(request.getDayNumber());

        validateDateInsideTrip(
                request.getDate(),
                trip
        );

        TripDay tripDay = new TripDay(
                trip,
                request.getDate(),
                request.getDayNumber(),
                clean(request.getNotes())
        );

        TripDay savedTripDay = tripDayRepository.save(tripDay);

        return toResponse(savedTripDay);
    }

    public List<TripDayResponse> getTripDays(Long tripId) {

        findTrip(tripId);

        return tripDayRepository
                .findByTripIdOrderByDayNumberAsc(tripId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TripDayResponse getTripDay(
            Long tripId,
            Long dayId
    ) {

        TripDay tripDay = findTripDay(dayId);

        validateOwnership(tripDay, tripId);

        return toResponse(tripDay);
    }

    public TripDayResponse updateTripDay(
            Long tripId,
            Long dayId,
            UpdateTripDayRequest request
    ) {

        Trip trip = findTrip(tripId);

        TripDay tripDay = findTripDay(dayId);

        validateOwnership(tripDay, tripId);

        validateDayNumber(request.getDayNumber());

        validateDateInsideTrip(
                request.getDate(),
                trip
        );

        tripDay.setDate(request.getDate());
        tripDay.setDayNumber(request.getDayNumber());
        tripDay.setNotes(clean(request.getNotes()));

        TripDay updatedTripDay =
                tripDayRepository.save(tripDay);

        return toResponse(updatedTripDay);
    }

    public void deleteTripDay(
            Long tripId,
            Long dayId
    ) {

        TripDay tripDay = findTripDay(dayId);

        validateOwnership(tripDay, tripId);

        tripDayRepository.delete(tripDay);
    }

    private Trip findTrip(Long tripId) {

        return tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip not found"
                        ));
    }

    private TripDay findTripDay(Long dayId) {

        return tripDayRepository.findById(dayId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Trip day not found"
                        ));
    }

    private void validateOwnership(
            TripDay tripDay,
            Long tripId
    ) {

        if (!tripDay.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException(
                    "Trip day does not belong to this trip"
            );
        }
    }

    private void validateDayNumber(Integer dayNumber) {

        if (dayNumber <= 0) {
            throw new IllegalArgumentException(
                    "Day number must be greater than 0"
            );
        }
    }

    private void validateDateInsideTrip(
            java.time.LocalDate date,
            Trip trip
    ) {

        if (date.isBefore(trip.getStartDate())
                || date.isAfter(trip.getEndDate())) {

            throw new IllegalArgumentException(
                    "Trip day date must be within trip dates"
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

    private TripDayResponse toResponse(
            TripDay tripDay
    ) {

        return new TripDayResponse(
                tripDay.getId(),
                tripDay.getTrip().getId(),
                tripDay.getDate(),
                tripDay.getDayNumber(),
                tripDay.getNotes()
        );
    }
}