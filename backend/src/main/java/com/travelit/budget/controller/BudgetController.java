package com.travelit.budget.controller;

import com.travelit.budget.dto.BudgetResponse;
import com.travelit.budget.dto.CreateBudgetItemRequest;
import com.travelit.budget.dto.CreateBudgetRequest;
import com.travelit.budget.dto.UpdateBudgetItemRequest;
import com.travelit.budget.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @Valid @RequestBody CreateBudgetRequest request
    ) {
        return ResponseEntity.ok(
                budgetService.createBudget(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets() {
        return ResponseEntity.ok(
                budgetService.getAllBudgets()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudget(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                budgetService.getBudget(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id
    ) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{budgetId}/items")
    public ResponseEntity<BudgetResponse> addBudgetItem(
            @PathVariable Long budgetId,
            @Valid @RequestBody CreateBudgetItemRequest request
    ) {
        return ResponseEntity.ok(
                budgetService.addBudgetItem(budgetId, request)
        );
    }

    @PutMapping("/{budgetId}/items/{itemId}")
    public ResponseEntity<BudgetResponse> updateBudgetItem(
            @PathVariable Long budgetId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateBudgetItemRequest request
    ) {
        return ResponseEntity.ok(
                budgetService.updateBudgetItem(
                        budgetId,
                        itemId,
                        request
                )
        );
    }

    @DeleteMapping("/{budgetId}/items/{itemId}")
    public ResponseEntity<BudgetResponse> removeBudgetItem(
            @PathVariable Long budgetId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                budgetService.removeBudgetItem(
                        budgetId,
                        itemId
                )
        );
    }
}