package com.bankingapp.controller;

import com.bankingapp.dto.request.CreateAccountRequest;
import com.bankingapp.dto.response.AccountResponse;
import com.bankingapp.entity.AccountStatus;
import com.bankingapp.entity.AccountType;
import com.bankingapp.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void createAccount_Success() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest(1L, AccountType.CHECKING, new BigDecimal("250.00"), "USD");
        AccountResponse response = new AccountResponse(
                1L, "9876543210", AccountType.CHECKING, new BigDecimal("250.00"),
                "USD", AccountStatus.ACTIVE, 1L, "Alice Smith", OffsetDateTime.now()
        );

        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accountNumber").value("9876543210"))
                .andExpect(jsonPath("$.data.balance").value(250.00));
    }

    @Test
    void getAccountsByUserId_Success() throws Exception {
        AccountResponse response = new AccountResponse(
                1L, "9876543210", AccountType.CHECKING, new BigDecimal("250.00"),
                "USD", AccountStatus.ACTIVE, 1L, "Alice Smith", OffsetDateTime.now()
        );

        when(accountService.getAccountsByUserId(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/accounts/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].accountNumber").value("9876543210"));
    }
}
