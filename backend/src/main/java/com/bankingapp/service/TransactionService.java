package com.bankingapp.service;

import com.bankingapp.dto.request.DepositRequest;
import com.bankingapp.dto.request.TransferRequest;
import com.bankingapp.dto.request.WithdrawRequest;
import com.bankingapp.dto.response.TransactionResponse;
import com.bankingapp.entity.Account;
import com.bankingapp.entity.AccountStatus;
import com.bankingapp.entity.Transaction;
import com.bankingapp.entity.TransactionStatus;
import com.bankingapp.entity.TransactionType;
import com.bankingapp.exception.InsufficientBalanceException;
import com.bankingapp.exception.InvalidTransactionException;
import com.bankingapp.exception.ResourceNotFoundException;
import com.bankingapp.repository.AccountRepository;
import com.bankingapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Deposit amount must be greater than zero");
        }

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + request.getAccountNumber()));

        validateAccountActive(account);

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(generateReference("DEP"));
        transaction.setTargetAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Account Deposit");

        Transaction saved = transactionRepository.save(transaction);
        return mapToTransactionResponse(saved);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal amount must be greater than zero");
        }

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + request.getAccountNumber()));

        validateAccountActive(account);

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient funds in account: " + request.getAccountNumber()
                    + ". Available: " + account.getBalance() + ", Requested: " + request.getAmount());
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(generateReference("WTH"));
        transaction.setSourceAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Account Withdrawal");

        Transaction saved = transactionRepository.save(transaction);
        return mapToTransactionResponse(saved);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transfer amount must be greater than zero");
        }

        if (request.getSourceAccountNumber().equals(request.getTargetAccountNumber())) {
            throw new InvalidTransactionException("Source and target accounts cannot be the same");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found: " + request.getSourceAccountNumber()));

        Account targetAccount = accountRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Target account not found: " + request.getTargetAccountNumber()));

        validateAccountActive(sourceAccount);
        validateAccountActive(targetAccount);

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient funds in source account: " + request.getSourceAccountNumber()
                    + ". Available: " + sourceAccount.getBalance() + ", Requested: " + request.getAmount());
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        targetAccount.setBalance(targetAccount.getBalance().add(request.getAmount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(generateReference("TRF"));
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Account Transfer");

        Transaction saved = transactionRepository.save(transaction);
        return mapToTransactionResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));

        return transactionRepository.findBySourceAccountOrTargetAccountOrderByTimestampDesc(account, account)
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByReference(String reference) {
        Transaction transaction = transactionRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + reference));
        return mapToTransactionResponse(transaction);
    }

    private void validateAccountActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Account " + account.getAccountNumber() + " is " + account.getStatus() + " and cannot process transactions");
        }
    }

    private String generateReference(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getAccountNumber() : null,
                transaction.getTargetAccount() != null ? transaction.getTargetAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getTimestamp()
        );
    }
}
