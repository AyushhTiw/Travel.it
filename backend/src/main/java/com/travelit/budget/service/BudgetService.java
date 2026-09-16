package com.travelit.budget.service;

import com.travelit.budget.dto.BudgetItemResponse;
import com.travelit.budget.dto.BudgetResponse;
import com.travelit.budget.dto.CreateBudgetItemRequest;
import com.travelit.budget.dto.CreateBudgetRequest;
import com.travelit.budget.dto.UpdateBudgetItemRequest;
import com.travelit.budget.entity.Budget;
import com.travelit.budget.entity.BudgetItem;
import com.travelit.budget.repository.BudgetItemRepository;
import com.travelit.budget.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetItemRepository budgetItemRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            BudgetItemRepository budgetItemRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.budgetItemRepository = budgetItemRepository;
    }

    @Transactional
    public BudgetResponse createBudget(CreateBudgetRequest request) {

        Budget budget = new Budget(
                request.getTotalAmount(),
                request.getCurrency().toUpperCase()
        );

        Budget savedBudget = budgetRepository.save(budget);

        return toBudgetResponse(savedBudget);
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudget(Long id) {

        Budget budget = findBudget(id);

        return toBudgetResponse(budget);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets() {

        return budgetRepository.findAll()
                .stream()
                .map(this::toBudgetResponse)
                .toList();
    }

    @Transactional
    public void deleteBudget(Long id) {

        Budget budget = findBudget(id);

        budgetRepository.delete(budget);
    }

    @Transactional
    public BudgetResponse addBudgetItem(
            Long budgetId,
            CreateBudgetItemRequest request
    ) {

        Budget budget = findBudget(budgetId);

        BudgetItem item = new BudgetItem(
                request.getCategory().trim(),
                request.getAmount(),
                request.getDescription()
        );

        budget.addItem(item);

        Budget savedBudget = budgetRepository.save(budget);

        return toBudgetResponse(savedBudget);
    }

    @Transactional
    public BudgetResponse updateBudgetItem(
            Long budgetId,
            Long itemId,
            UpdateBudgetItemRequest request
    ) {

        Budget budget = findBudget(budgetId);

        BudgetItem item = budget.getItems()
                .stream()
                .filter(existingItem ->
                        existingItem.getId().equals(itemId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Budget item not found")
                );

        item.setCategory(request.getCategory().trim());
        item.setAmount(request.getAmount());
        item.setDescription(request.getDescription());

        return toBudgetResponse(budget);
    }

    @Transactional
    public BudgetResponse removeBudgetItem(
            Long budgetId,
            Long itemId
    ) {

        Budget budget = findBudget(budgetId);

        BudgetItem item = budget.getItems()
                .stream()
                .filter(existingItem ->
                        existingItem.getId().equals(itemId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Budget item not found")
                );

        budget.removeItem(item);

        return toBudgetResponse(budget);
    }

    private Budget findBudget(Long id) {

        return budgetRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Budget not found")
                );
    }

    private BudgetResponse toBudgetResponse(Budget budget) {

        List<BudgetItemResponse> items = budget.getItems()
                .stream()
                .map(item -> new BudgetItemResponse(
                        item.getId(),
                        item.getCategory(),
                        item.getAmount(),
                        item.getDescription()
                ))
                .toList();

        return new BudgetResponse(
                budget.getId(),
                budget.getTotalAmount(),
                budget.getCurrency(),
                items
        );
    }
}