package com.bankingapp.service;

import com.bankingapp.dto.request.CreateAccountRequest;
import com.bankingapp.dto.response.AccountResponse;
import com.bankingapp.entity.Account;
import com.bankingapp.entity.AccountStatus;
import com.bankingapp.entity.AccountType;
import com.bankingapp.entity.User;
import com.bankingapp.exception.ResourceNotFoundException;
import com.bankingapp.repository.AccountRepository;
import com.bankingapp.repository.UserRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    private User sampleUser;
    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "john_doe", "john@example.com", "secret", "John Doe");
        sampleAccount = new Account(1L, "1234567890", AccountType.SAVINGS, new BigDecimal("1000.00"), "USD", AccountStatus.ACTIVE, sampleUser);
    }

    @Test
    void createAccount_Success() {
        CreateAccountRequest request = new CreateAccountRequest(1L, AccountType.SAVINGS, new BigDecimal("500.00"), "USD");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(10L);
            return a;
        });

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("500.00"), response.getBalance());
        assertEquals(AccountType.SAVINGS, response.getAccountType());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void getAccountByNumber_Success() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(sampleAccount));

        AccountResponse response = accountService.getAccountByNumber("1234567890");

        assertNotNull(response);
        assertEquals("1234567890", response.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), response.getBalance());
    }

    @Test
    void getAccountByNumber_NotFound_ThrowsException() {
        when(accountRepository.findByAccountNumber("9999999999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountByNumber("9999999999"));
    }
}
