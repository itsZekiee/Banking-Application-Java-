package com.bankingapp.dto.request;

import com.bankingapp.entity.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateAccountRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @DecimalMin(value = "0.00", message = "Initial deposit cannot be negative")
    private BigDecimal initialDeposit = BigDecimal.ZERO;

    private String currency = "USD";

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(Long userId, AccountType accountType, BigDecimal initialDeposit, String currency) {
        this.userId = userId;
        this.accountType = accountType;
        this.initialDeposit = initialDeposit != null ? initialDeposit : BigDecimal.ZERO;
        this.currency = (currency != null && !currency.isBlank()) ? currency : "USD";
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
