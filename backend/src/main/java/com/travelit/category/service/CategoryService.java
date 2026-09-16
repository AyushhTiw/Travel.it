package com.travelit.category.service;

import com.travelit.category.dto.CategoryResponse;
import com.travelit.category.dto.CreateCategoryRequest;
import com.travelit.category.dto.UpdateCategoryRequest;
import com.travelit.category.entity.Category;
import com.travelit.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(
            CreateCategoryRequest request
    ) {
        String name = request.getName().trim();

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "Category already exists"
            );
        }

        Category category = new Category(
                name,
                clean(request.getDescription())
        );

        Category savedCategory = categoryRepository.save(category);

        return toResponse(savedCategory);
    }

    public CategoryResponse getCategory(Long id) {

        Category category = findCategory(id);

        return toResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CategoryResponse> getActiveCategories() {

        return categoryRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse updateCategory(
            Long id,
            UpdateCategoryRequest request
    ) {
        Category category = findCategory(id);

        String name = request.getName().trim();

        boolean nameChanged =
                !category.getName().equalsIgnoreCase(name);

        if (nameChanged &&
                categoryRepository.existsByNameIgnoreCase(name)) {

            throw new IllegalArgumentException(
                    "Category already exists"
            );
        }

        category.setName(name);
        category.setDescription(
                clean(request.getDescription())
        );

        Category updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    public CategoryResponse deactivateCategory(Long id) {

        Category category = findCategory(id);

        category.setActive(false);

        Category updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    public CategoryResponse activateCategory(Long id) {

        Category category = findCategory(id);

        category.setActive(true);

        Category updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {

        Category category = findCategory(id);

        categoryRepository.delete(category);
    }

    private Category findCategory(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found"
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

    private CategoryResponse toResponse(
            Category category
    ) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive()
        );
    }
}