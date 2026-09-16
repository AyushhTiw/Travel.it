package com.travelit.destination.service;

import com.travelit.destination.dto.CreateDestinationRequest;
import com.travelit.destination.dto.DestinationResponse;
import com.travelit.destination.dto.UpdateDestinationRequest;
import com.travelit.destination.entity.Destination;
import com.travelit.destination.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public DestinationResponse createDestination(
            CreateDestinationRequest request
    ) {
        String name = request.getName().trim();
        String country = request.getCountry().trim();

        if (destinationRepository
                .existsByNameIgnoreCaseAndCountryIgnoreCase(name, country)) {

            throw new IllegalArgumentException(
                    "Destination already exists"
            );
        }

        Destination destination = new Destination(
                name,
                country,
                clean(request.getState()),
                clean(request.getDescription())
        );

        Destination savedDestination =
                destinationRepository.save(destination);

        return toResponse(savedDestination);
    }

    public DestinationResponse getDestination(Long id) {

        Destination destination = findDestination(id);

        return toResponse(destination);
    }

    public List<DestinationResponse> getAllDestinations() {

        return destinationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DestinationResponse updateDestination(
            Long id,
            UpdateDestinationRequest request
    ) {
        Destination destination = findDestination(id);

        String name = request.getName().trim();
        String country = request.getCountry().trim();

        boolean nameChanged =
                !destination.getName().equalsIgnoreCase(name)
                        || !destination.getCountry().equalsIgnoreCase(country);

        if (nameChanged &&
                destinationRepository
                        .existsByNameIgnoreCaseAndCountryIgnoreCase(
                                name,
                                country
                        )) {

            throw new IllegalArgumentException(
                    "Destination already exists"
            );
        }

        destination.setName(name);
        destination.setCountry(country);
        destination.setState(clean(request.getState()));
        destination.setDescription(clean(request.getDescription()));

        Destination updatedDestination =
                destinationRepository.save(destination);

        return toResponse(updatedDestination);
    }

    public void deleteDestination(Long id) {

        Destination destination = findDestination(id);

        destinationRepository.delete(destination);
    }

    private Destination findDestination(Long id) {

        return destinationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Destination not found"
                        )
                );
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        String cleaned = value.trim();

        return cleaned.isEmpty() ? null : cleaned;
    }

    private DestinationResponse toResponse(
            Destination destination
    ) {
        return new DestinationResponse(
                destination.getId(),
                destination.getName(),
                destination.getCountry(),
                destination.getState(),
                destination.getDescription()
        );
    }
}