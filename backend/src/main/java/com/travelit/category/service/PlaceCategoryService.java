package com.travelit.category.service;

import com.travelit.category.dto.AssignCategoryRequest;
import com.travelit.category.entity.PlaceCategory;
import com.travelit.category.repository.PlaceCategoryRepository;
import com.travelit.place.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceCategoryService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceService placeService;
    private final CategoryService categoryService;

    public PlaceCategoryService(
            PlaceCategoryRepository placeCategoryRepository,
            PlaceService placeService,
            CategoryService categoryService
    ) {
        this.placeCategoryRepository = placeCategoryRepository;
        this.placeService = placeService;
        this.categoryService = categoryService;
    }

    public PlaceCategory assignCategory(
            AssignCategoryRequest request
    ) {

        placeService.getPlace(request.getPlaceId());

        categoryService.getCategory(request.getCategoryId());

        if (placeCategoryRepository.existsByPlaceIdAndCategoryId(
                request.getPlaceId(),
                request.getCategoryId()
        )) {
            throw new IllegalArgumentException(
                    "Category is already assigned to this place"
            );
        }

        PlaceCategory placeCategory =
                new PlaceCategory(
                        request.getPlaceId(),
                        request.getCategoryId()
                );

        return placeCategoryRepository.save(placeCategory);
    }

    public List<PlaceCategory> getCategoriesForPlace(
            Long placeId
    ) {

        placeService.getPlace(placeId);

        return placeCategoryRepository.findByPlaceId(placeId);
    }

    public List<PlaceCategory> getPlacesForCategory(
            Long categoryId
    ) {

        categoryService.getCategory(categoryId);

        return placeCategoryRepository.findByCategoryId(categoryId);
    }

    public void removeCategoryFromPlace(
            Long placeId,
            Long categoryId
    ) {

        PlaceCategory placeCategory =
                placeCategoryRepository
                        .findByPlaceIdAndCategoryId(
                                placeId,
                                categoryId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Category is not assigned to this place"
                                ));

        placeCategoryRepository.delete(placeCategory);
    }
}