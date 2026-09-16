package com.travelit.destination.service;

import com.travelit.destination.dto.AssignPlaceToDestinationRequest;
import com.travelit.destination.entity.DestinationPlace;
import com.travelit.destination.repository.DestinationPlaceRepository;
import com.travelit.place.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationPlaceService {

    private final DestinationPlaceRepository destinationPlaceRepository;
    private final DestinationService destinationService;
    private final PlaceService placeService;

    public DestinationPlaceService(
            DestinationPlaceRepository destinationPlaceRepository,
            DestinationService destinationService,
            PlaceService placeService
    ) {
        this.destinationPlaceRepository = destinationPlaceRepository;
        this.destinationService = destinationService;
        this.placeService = placeService;
    }

    public DestinationPlace assignPlace(
            AssignPlaceToDestinationRequest request
    ) {

        destinationService.getDestination(
                request.getDestinationId()
        );

        placeService.getPlace(
                request.getPlaceId()
        );

        if (destinationPlaceRepository
                .existsByDestinationIdAndPlaceId(
                        request.getDestinationId(),
                        request.getPlaceId()
                )) {

            throw new IllegalArgumentException(
                    "Place is already assigned to this destination"
            );
        }

        DestinationPlace destinationPlace =
                new DestinationPlace(
                        request.getDestinationId(),
                        request.getPlaceId()
                );

        return destinationPlaceRepository.save(
                destinationPlace
        );
    }

    public List<DestinationPlace> getPlacesForDestination(
            Long destinationId
    ) {

        destinationService.getDestination(destinationId);

        return destinationPlaceRepository
                .findByDestinationId(destinationId);
    }

    public List<DestinationPlace> getDestinationsForPlace(
            Long placeId
    ) {

        placeService.getPlace(placeId);

        return destinationPlaceRepository
                .findByPlaceId(placeId);
    }

    public void removePlaceFromDestination(
            Long destinationId,
            Long placeId
    ) {

        DestinationPlace destinationPlace =
                destinationPlaceRepository
                        .findByDestinationIdAndPlaceId(
                                destinationId,
                                placeId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Place is not assigned to this destination"
                                ));

        destinationPlaceRepository.delete(
                destinationPlace
        );
    }
}