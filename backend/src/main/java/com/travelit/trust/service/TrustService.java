package com.travelit.trust.service;

import com.travelit.auth.entity.User;
import com.travelit.place.service.PlaceService;
import com.travelit.trust.dto.CreatePlaceSourceRequest;
import com.travelit.trust.dto.CreatePlaceVerificationRequest;
import com.travelit.trust.entity.PlaceSource;
import com.travelit.trust.entity.PlaceVerification;
import com.travelit.trust.repository.PlaceSourceRepository;
import com.travelit.trust.repository.PlaceVerificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrustService {

    private final PlaceSourceRepository sourceRepository;
    private final PlaceVerificationRepository verificationRepository;
    private final PlaceService placeService;

    public TrustService(
            PlaceSourceRepository sourceRepository,
            PlaceVerificationRepository verificationRepository,
            PlaceService placeService
    ) {
        this.sourceRepository = sourceRepository;
        this.verificationRepository = verificationRepository;
        this.placeService = placeService;
    }

    public PlaceSource addSource(
            Authentication authentication,
            CreatePlaceSourceRequest request
    ) {

        requireAdmin(authentication);

        placeService.getPlace(request.getPlaceId());

        PlaceSource source = new PlaceSource(
                request.getPlaceId(),
                request.getSourceName().trim(),
                clean(request.getSourceUrl()),
                clean(request.getSourceDescription())
        );

        return sourceRepository.save(source);
    }

    public List<PlaceSource> getSources(Long placeId) {

        placeService.getPlace(placeId);

        return sourceRepository
                .findByPlaceIdOrderByCreatedAtDesc(placeId);
    }

    public PlaceVerification addVerification(
            Authentication authentication,
            CreatePlaceVerificationRequest request
    ) {

        requireAdmin(authentication);

        placeService.getPlace(request.getPlaceId());

        PlaceVerification verification =
                new PlaceVerification(
                        request.getPlaceId(),
                        request.getVerificationType().trim(),
                        request.getVerified(),
                        clean(request.getNotes())
                );

        return verificationRepository.save(verification);
    }

    public List<PlaceVerification> getVerifications(
            Long placeId
    ) {

        placeService.getPlace(placeId);

        return verificationRepository
                .findByPlaceIdOrderByVerifiedAtDesc(placeId);
    }

    public List<PlaceVerification> getVerifiedVerifications(
            Long placeId
    ) {

        placeService.getPlace(placeId);

        return verificationRepository
                .findByPlaceIdAndVerifiedTrue(placeId);
    }

    public void deleteSource(
            Authentication authentication,
            Long sourceId
    ) {

        requireAdmin(authentication);

        PlaceSource source = sourceRepository
                .findById(sourceId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Place source not found"
                        ));

        sourceRepository.delete(source);
    }

    public void deleteVerification(
            Authentication authentication,
            Long verificationId
    ) {

        requireAdmin(authentication);

        PlaceVerification verification =
                verificationRepository
                        .findById(verificationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Place verification not found"
                                ));

        verificationRepository.delete(verification);
    }

    private void requireAdmin(Authentication authentication) {

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            throw new IllegalArgumentException(
                    "Authenticated user not found"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException(
                    "Admin access required"
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
}