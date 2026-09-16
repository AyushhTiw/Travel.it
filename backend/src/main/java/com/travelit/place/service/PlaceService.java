package com.travelit.place.service;

import com.travelit.place.dto.CreatePlaceRequest;
import com.travelit.place.dto.PlaceResponse;
import com.travelit.place.dto.UpdatePlaceRequest;
import com.travelit.place.entity.Place;
import com.travelit.place.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public PlaceResponse createPlace(CreatePlaceRequest request) {

        Place place = new Place(
                request.getName().trim(),
                clean(request.getDescription()),
                request.getCity().trim(),
                clean(request.getState()),
                request.getCountry().trim(),
                request.getLatitude(),
                request.getLongitude(),
                clean(request.getAddress())
        );

        Place savedPlace = placeRepository.save(place);

        return toResponse(savedPlace);
    }

    public PlaceResponse getPlace(Long id) {

        Place place = findPlace(id);

        return toResponse(place);
    }

    public List<PlaceResponse> getAllPlaces() {

        return placeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PlaceResponse> getPlacesByCity(String city) {

        return placeRepository.findByCityIgnoreCase(city.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PlaceResponse> getPlacesByCountry(String country) {

        return placeRepository.findByCountryIgnoreCase(country.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PlaceResponse> getActivePlaces() {

        return placeRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlaceResponse updatePlace(
            Long id,
            UpdatePlaceRequest request
    ) {

        Place place = findPlace(id);

        place.setName(request.getName().trim());
        place.setDescription(clean(request.getDescription()));
        place.setCity(request.getCity().trim());
        place.setState(clean(request.getState()));
        place.setCountry(request.getCountry().trim());
        place.setLatitude(request.getLatitude());
        place.setLongitude(request.getLongitude());
        place.setAddress(clean(request.getAddress()));

        Place updatedPlace = placeRepository.save(place);

        return toResponse(updatedPlace);
    }

    public void deletePlace(Long id) {

        Place place = findPlace(id);

        placeRepository.delete(place);
    }

    private Place findPlace(Long id) {

        return placeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Place not found")
                );
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        String cleaned = value.trim();

        return cleaned.isEmpty() ? null : cleaned;
    }

    private PlaceResponse toResponse(Place place) {

        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getDescription(),
                place.getCity(),
                place.getState(),
                place.getCountry(),
                place.getLatitude(),
                place.getLongitude(),
                place.getAddress(),
                place.isActive()
        );
    }
}