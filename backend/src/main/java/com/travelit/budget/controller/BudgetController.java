package com.travelit.budget.controller;

import com.travelit.budget.dto.BudgetResponse;
import com.travelit.budget.dto.CreateBudgetItemRequest;
import com.travelit.budget.dto.CreateBudgetRequest;
import com.travelit.budget.dto.UpdateBudgetItemRequest;
import com.travelit.budget.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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
            @Valid @RequestBody CreateBudgetRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                budgetService.createBudget(request, auth)
        );
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(Authentication auth) {
        return ResponseEntity.ok(
                budgetService.getAllBudgets(auth)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudget(
            @PathVariable Long id,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                budgetService.getBudget(id, auth)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id,
            Authentication auth
    ) {
        budgetService.deleteBudget(id, auth);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{budgetId}/items")
    public ResponseEntity<BudgetResponse> addBudgetItem(
            @PathVariable Long budgetId,
            @Valid @RequestBody CreateBudgetItemRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                budgetService.addBudgetItem(budgetId, request, auth)
        );
    }

    @PutMapping("/{budgetId}/items/{itemId}")
    public ResponseEntity<BudgetResponse> updateBudgetItem(
            @PathVariable Long budgetId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateBudgetItemRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                budgetService.updateBudgetItem(
                        budgetId,
                        itemId,
                        request,
                        auth
                )
        );
    }

    @DeleteMapping("/{budgetId}/items/{itemId}")
    public ResponseEntity<BudgetResponse> removeBudgetItem(
            @PathVariable Long budgetId,
            @PathVariable Long itemId,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                budgetService.removeBudgetItem(
                        budgetId,
                        itemId,
                        auth
                )
        );
    }
}