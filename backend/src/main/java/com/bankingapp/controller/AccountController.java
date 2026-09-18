package com.bankingapp.controller;

import com.bankingapp.dto.request.CreateAccountRequest;
import com.bankingapp.dto.response.AccountResponse;
import com.bankingapp.dto.response.ApiResponse;
import com.bankingapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(ApiResponse.ok("Account created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountByNumber(@PathVariable String accountNumber) {
        AccountResponse response = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Account retrieved successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsByUserId(@PathVariable Long userId) {
        List<AccountResponse> response = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok("User accounts retrieved successfully", response));
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Balance retrieved successfully", Map.of(
                "accountNumber", accountNumber,
                "balance", balance
        )));
    }
}
