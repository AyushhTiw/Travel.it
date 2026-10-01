package com.travelit.budget.service;

import com.travelit.auth.entity.User;

import com.travelit.budget.dto.BudgetItemResponse;
import com.travelit.budget.dto.BudgetResponse;
import com.travelit.budget.dto.CreateBudgetItemRequest;
import com.travelit.budget.dto.CreateBudgetRequest;
import com.travelit.budget.dto.UpdateBudgetItemRequest;
import com.travelit.budget.entity.Budget;
import com.travelit.budget.entity.BudgetItem;
import com.travelit.budget.repository.BudgetItemRepository;
import com.travelit.budget.repository.BudgetRepository;
import org.springframework.security.core.Authentication;
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
    public BudgetResponse createBudget(CreateBudgetRequest request, Authentication auth) {
        User user = requireUser(auth);

        Budget budget = new Budget(
                user.getId(),
                request.getTotalAmount(),
                request.getCurrency().toUpperCase()
        );

        Budget savedBudget = budgetRepository.save(budget);

        return toBudgetResponse(savedBudget);
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudget(Long id, Authentication auth) {
        User user = requireUser(auth);
        Budget budget = findOwned(id, user.getId());

        return toBudgetResponse(budget);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets(Authentication auth) {
        User user = requireUser(auth);
        return budgetRepository.findByUserId(user.getId())
                .stream()
                .map(this::toBudgetResponse)
                .toList();
    }

    @Transactional
    public void deleteBudget(Long id, Authentication auth) {
        User user = requireUser(auth);
        Budget budget = findOwned(id, user.getId());

        budgetRepository.delete(budget);
    }

    @Transactional
    public BudgetResponse addBudgetItem(
            Long budgetId,
            CreateBudgetItemRequest request,
            Authentication auth
    ) {
        User user = requireUser(auth);
        Budget budget = findOwned(budgetId, user.getId());

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
            UpdateBudgetItemRequest request,
            Authentication auth
    ) {
        User user = requireUser(auth);
        Budget budget = findOwned(budgetId, user.getId());

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
            Long itemId,
            Authentication auth
    ) {
        User user = requireUser(auth);
        Budget budget = findOwned(budgetId, user.getId());

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

    private Budget findOwned(Long id, Long userId) {
        return budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Budget not found")
                );
    }

    private User requireUser(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User u)) {
            throw new IllegalArgumentException("Authentication required");
        }
        return u;
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