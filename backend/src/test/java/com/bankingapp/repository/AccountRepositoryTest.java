package com.bankingapp.repository;

import com.bankingapp.entity.Account;
import com.bankingapp.entity.AccountStatus;
import com.bankingapp.entity.AccountType;
import com.bankingapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findByAccountNumber_ReturnsAccount() {
        User user = new User(null, "johndoe", "john@example.com", "hash123", "John Doe");
        user = entityManager.persistAndFlush(user);

        Account account = new Account(null, "1234567890", AccountType.SAVINGS, new BigDecimal("1000.00"), "USD", AccountStatus.ACTIVE, user);
        entityManager.persistAndFlush(account);

        Optional<Account> found = accountRepository.findByAccountNumber("1234567890");

        assertTrue(found.isPresent());
        assertEquals("1234567890", found.get().getAccountNumber());
        assertEquals(user.getId(), found.get().getUser().getId());
    }

    @Test
    void findByUserId_ReturnsUserAccounts() {
        User user = new User(null, "janedoe", "jane@example.com", "hash123", "Jane Doe");
        user = entityManager.persistAndFlush(user);

        Account account1 = new Account(null, "1111111111", AccountType.SAVINGS, new BigDecimal("500.00"), "USD", AccountStatus.ACTIVE, user);
        Account account2 = new Account(null, "2222222222", AccountType.CHECKING, new BigDecimal("300.00"), "USD", AccountStatus.ACTIVE, user);
        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);

        List<Account> accounts = accountRepository.findByUserId(user.getId());

        assertEquals(2, accounts.size());
    }
}
