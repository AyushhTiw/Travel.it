package com.travelit.trust.controller;

import com.travelit.trust.dto.CreatePlaceSourceRequest;
import com.travelit.trust.dto.CreatePlaceVerificationRequest;
import com.travelit.trust.entity.PlaceSource;
import com.travelit.trust.entity.PlaceVerification;
import com.travelit.trust.service.TrustService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trust")
public class TrustController {

    private final TrustService trustService;

    public TrustController(TrustService trustService) {
        this.trustService = trustService;
    }

    @PostMapping("/sources")
    public ResponseEntity<PlaceSource> addSource(
            Authentication authentication,
            @Valid @RequestBody CreatePlaceSourceRequest request
    ) {

        return ResponseEntity.ok(
                trustService.addSource(
                        authentication,
                        request
                )
        );
    }

    @GetMapping("/places/{placeId}/sources")
    public ResponseEntity<List<PlaceSource>> getSources(
            @PathVariable Long placeId
    ) {

        return ResponseEntity.ok(
                trustService.getSources(placeId)
        );
    }

    @PostMapping("/verifications")
    public ResponseEntity<PlaceVerification> addVerification(
            Authentication authentication,
            @Valid @RequestBody CreatePlaceVerificationRequest request
    ) {

        return ResponseEntity.ok(
                trustService.addVerification(
                        authentication,
                        request
                )
        );
    }

    @GetMapping("/places/{placeId}/verifications")
    public ResponseEntity<List<PlaceVerification>> getVerifications(
            @PathVariable Long placeId
    ) {

        return ResponseEntity.ok(
                trustService.getVerifications(placeId)
        );
    }

    @GetMapping("/places/{placeId}/verifications/verified")
    public ResponseEntity<List<PlaceVerification>>
    getVerifiedVerifications(
            @PathVariable Long placeId
    ) {

        return ResponseEntity.ok(
                trustService.getVerifiedVerifications(placeId)
        );
    }

    @DeleteMapping("/sources/{sourceId}")
    public ResponseEntity<Void> deleteSource(
            Authentication authentication,
            @PathVariable Long sourceId
    ) {

        trustService.deleteSource(
                authentication,
                sourceId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/verifications/{verificationId}")
    public ResponseEntity<Void> deleteVerification(
            Authentication authentication,
            @PathVariable Long verificationId
    ) {

        trustService.deleteVerification(
                authentication,
                verificationId
        );

        return ResponseEntity.noContent().build();
    }
}