package com.travelit.category.controller;

import com.travelit.category.dto.AssignCategoryRequest;
import com.travelit.category.dto.PlaceCategoryResponse;
import com.travelit.category.entity.PlaceCategory;
import com.travelit.category.service.PlaceCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/place-categories")
public class PlaceCategoryController {

    private final PlaceCategoryService placeCategoryService;

    public PlaceCategoryController(
            PlaceCategoryService placeCategoryService
    ) {
        this.placeCategoryService = placeCategoryService;
    }

    @PostMapping
    public ResponseEntity<PlaceCategoryResponse> assignCategory(
            @Valid @RequestBody AssignCategoryRequest request
    ) {

        PlaceCategory result =
                placeCategoryService.assignCategory(request);

        return ResponseEntity.ok(toResponse(result));
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<PlaceCategoryResponse>>
    getCategoriesForPlace(
            @PathVariable Long placeId
    ) {

        return ResponseEntity.ok(
                placeCategoryService
                        .getCategoriesForPlace(placeId)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PlaceCategoryResponse>>
    getPlacesForCategory(
            @PathVariable Long categoryId
    ) {

        return ResponseEntity.ok(
                placeCategoryService
                        .getPlacesForCategory(categoryId)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @DeleteMapping("/place/{placeId}/category/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromPlace(
            @PathVariable Long placeId,
            @PathVariable Long categoryId
    ) {

        placeCategoryService.removeCategoryFromPlace(
                placeId,
                categoryId
        );

        return ResponseEntity.noContent().build();
    }

    private PlaceCategoryResponse toResponse(
            PlaceCategory placeCategory
    ) {

        return new PlaceCategoryResponse(
                placeCategory.getId(),
                placeCategory.getPlaceId(),
                placeCategory.getCategoryId()
        );
    }
}