package com.travelit.budget.dto;

import java.math.BigDecimal;

public class BudgetItemResponse {

    private Long id;
    private String category;
    private BigDecimal amount;
    private String description;

    public BudgetItemResponse() {
    }

    public BudgetItemResponse(
            Long id,
            String category,
            BigDecimal amount,
            String description
    ) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}