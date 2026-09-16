package com.travelit.trip.service;

import com.travelit.trip.dto.CreateItineraryItemRequest;
import com.travelit.trip.dto.ItineraryItemResponse;
import com.travelit.trip.dto.UpdateItineraryItemRequest;
import com.travelit.trip.entity.ItineraryItem;
import com.travelit.trip.entity.TripDay;
import com.travelit.trip.repository.ItineraryItemRepository;
import com.travelit.trip.repository.TripDayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryItemService {

    private final ItineraryItemRepository itineraryItemRepository;
    private final TripDayRepository tripDayRepository;

    public ItineraryItemService(
            ItineraryItemRepository itineraryItemRepository,
            TripDayRepository tripDayRepository
    ) {
        this.itineraryItemRepository = itineraryItemRepository;
        this.tripDayRepository = tripDayRepository;
    }

    public ItineraryItemResponse createItem(
            Long tripDayId,
            CreateItineraryItemRequest request
    ) {
        TripDay tripDay = findTripDay(tripDayId);

        validateTimes(request.getStartTime(), request.getEndTime());

        ItineraryItem item = new ItineraryItem(
                tripDay,
                request.getTitle().trim(),
                clean(request.getDescription()),
                clean(request.getLocation()),
                request.getStartTime(),
                request.getEndTime(),
                request.getSortOrder()
        );

        return toResponse(itineraryItemRepository.save(item));
    }

    public List<ItineraryItemResponse> getItems(Long tripDayId) {

        findTripDay(tripDayId);

        return itineraryItemRepository
                .findByTripDayIdOrderBySortOrderAsc(tripDayId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ItineraryItemResponse getItem(
            Long tripDayId,
            Long itemId
    ) {
        ItineraryItem item = findItem(itemId);
        validateOwnership(item, tripDayId);

        return toResponse(item);
    }

    public ItineraryItemResponse updateItem(
            Long tripDayId,
            Long itemId,
            UpdateItineraryItemRequest request
    ) {
        ItineraryItem item = findItem(itemId);
        validateOwnership(item, tripDayId);

        validateTimes(request.getStartTime(), request.getEndTime());

        item.setTitle(request.getTitle().trim());
        item.setDescription(clean(request.getDescription()));
        item.setLocation(clean(request.getLocation()));
        item.setStartTime(request.getStartTime());
        item.setEndTime(request.getEndTime());
        item.setSortOrder(request.getSortOrder());

        return toResponse(itineraryItemRepository.save(item));
    }

    public void deleteItem(Long tripDayId, Long itemId) {

        ItineraryItem item = findItem(itemId);
        validateOwnership(item, tripDayId);

        itineraryItemRepository.delete(item);
    }

    private TripDay findTripDay(Long tripDayId) {

        return tripDayRepository.findById(tripDayId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Trip day not found"));
    }

    private ItineraryItem findItem(Long itemId) {

        return itineraryItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Itinerary item not found"));
    }

    private void validateOwnership(
            ItineraryItem item,
            Long tripDayId
    ) {
        if (!item.getTripDay().getId().equals(tripDayId)) {
            throw new IllegalArgumentException(
                    "Itinerary item does not belong to this trip day"
            );
        }
    }

    private void validateTimes(
            java.time.LocalTime startTime,
            java.time.LocalTime endTime
    ) {
        if (startTime != null
                && endTime != null
                && endTime.isBefore(startTime)) {

            throw new IllegalArgumentException(
                    "End time cannot be before start time"
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

    private ItineraryItemResponse toResponse(
            ItineraryItem item
    ) {
        return new ItineraryItemResponse(
                item.getId(),
                item.getTripDay().getId(),
                item.getTitle(),
                item.getDescription(),
                item.getLocation(),
                item.getStartTime(),
                item.getEndTime(),
                item.getSortOrder()
        );
    }
}