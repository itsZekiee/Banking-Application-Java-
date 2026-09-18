package com.bankingapp.service;

import com.bankingapp.dto.request.DepositRequest;
import com.bankingapp.dto.request.TransferRequest;
import com.bankingapp.dto.request.WithdrawRequest;
import com.bankingapp.dto.response.TransactionResponse;
import com.bankingapp.entity.Account;
import com.bankingapp.entity.AccountStatus;
import com.bankingapp.entity.AccountType;
import com.bankingapp.entity.Transaction;
import com.bankingapp.entity.TransactionStatus;
import com.bankingapp.entity.TransactionType;
import com.bankingapp.entity.User;
import com.bankingapp.exception.InsufficientBalanceException;
import com.bankingapp.repository.AccountRepository;
import com.bankingapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account sourceAccount;
    private Account targetAccount;

    @BeforeEach
    void setUp() {
        User user1 = new User(1L, "alice", "alice@example.com", "pass", "Alice Smith");
        User user2 = new User(2L, "bob", "bob@example.com", "pass", "Bob Jones");

        sourceAccount = new Account(1L, "1111111111", AccountType.CHECKING, new BigDecimal("500.00"), "USD", AccountStatus.ACTIVE, user1);
        targetAccount = new Account(2L, "2222222222", AccountType.SAVINGS, new BigDecimal("200.00"), "USD", AccountStatus.ACTIVE, user2);
    }

    @Test
    void deposit_Success() {
        DepositRequest request = new DepositRequest("1111111111", new BigDecimal("100.00"), "Deposit cash");

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(101L);
            return t;
        });

        TransactionResponse response = transactionService.deposit(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals(TransactionType.DEPOSIT, response.getTransactionType());
        assertEquals(TransactionStatus.SUCCESS, response.getStatus());
        assertEquals(new BigDecimal("600.00"), sourceAccount.getBalance());
        verify(accountRepository).save(sourceAccount);
    }

    @Test
    void withdraw_Success() {
        WithdrawRequest request = new WithdrawRequest("1111111111", new BigDecimal("150.00"), "ATM withdrawal");

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(102L);
            return t;
        });

        TransactionResponse response = transactionService.withdraw(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("150.00"), response.getAmount());
        assertEquals(new BigDecimal("350.00"), sourceAccount.getBalance());
        verify(accountRepository).save(sourceAccount);
    }

    @Test
    void withdraw_InsufficientFunds_ThrowsException() {
        WithdrawRequest request = new WithdrawRequest("1111111111", new BigDecimal("1000.00"), "Overdraft");

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.withdraw(request));
    }

    @Test
    void transfer_Success() {
        TransferRequest request = new TransferRequest("1111111111", "2222222222", new BigDecimal("200.00"), "Rent");

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(targetAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(103L);
            return t;
        });

        TransactionResponse response = transactionService.transfer(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("200.00"), response.getAmount());
        assertEquals(new BigDecimal("300.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("400.00"), targetAccount.getBalance());
        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(targetAccount);
    }
}
