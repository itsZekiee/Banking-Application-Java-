package com.bankingapp.controller;

import com.bankingapp.dto.request.DepositRequest;
import com.bankingapp.dto.request.TransferRequest;
import com.bankingapp.dto.request.WithdrawRequest;
import com.bankingapp.dto.response.ApiResponse;
import com.bankingapp.dto.response.TransactionResponse;
import com.bankingapp.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(request);
        return new ResponseEntity<>(ApiResponse.ok("Deposit successful", response), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(@Valid @RequestBody WithdrawRequest request) {
        TransactionResponse response = transactionService.withdraw(request);
        return new ResponseEntity<>(ApiResponse.ok("Withdrawal successful", response), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.transfer(request);
        return new ResponseEntity<>(ApiResponse.ok("Transfer successful", response), HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionHistory(@PathVariable String accountNumber) {
        List<TransactionResponse> response = transactionService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(ApiResponse.ok("Transaction history retrieved successfully", response));
    }

    @GetMapping("/{reference}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionByReference(@PathVariable String reference) {
        TransactionResponse response = transactionService.getTransactionByReference(reference);
        return ResponseEntity.ok(ApiResponse.ok("Transaction retrieved successfully", response));
    }
}
