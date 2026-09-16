package com.travelit.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public class BudgetResponse {

    private Long id;
    private BigDecimal totalAmount;
    private String currency;
    private List<BudgetItemResponse> items;

    public BudgetResponse() {
    }

    public BudgetResponse(
            Long id,
            BigDecimal totalAmount,
            String currency,
            List<BudgetItemResponse> items
    ) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public List<BudgetItemResponse> getItems() {
        return items;
    }
}