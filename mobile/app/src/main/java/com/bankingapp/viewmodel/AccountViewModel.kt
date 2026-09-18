package com.bankingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bankingapp.data.model.AccountResponse
import com.bankingapp.data.model.AccountType
import com.bankingapp.data.model.CreateAccountRequest
import com.bankingapp.data.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

sealed interface AccountListUiState {
    object Idle : AccountListUiState
    object Loading : AccountListUiState
    data class Success(val accounts: List<AccountResponse>) : AccountListUiState
    data class Error(val message: String) : AccountListUiState
}

sealed interface CreateAccountUiState {
    object Idle : CreateAccountUiState
    object Loading : CreateAccountUiState
    data class Success(val account: AccountResponse) : CreateAccountUiState
    data class Error(val message: String) : CreateAccountUiState
}

sealed interface BalanceUiState {
    object Idle : BalanceUiState
    object Loading : BalanceUiState
    data class Success(val accountNumber: String, val balance: BigDecimal) : BalanceUiState
    data class Error(val message: String) : BalanceUiState
}

class AccountViewModel(
    private val accountRepository: AccountRepository = AccountRepository()
) : ViewModel() {

    private val _accountListState = MutableStateFlow<AccountListUiState>(AccountListUiState.Idle)
    val accountListState: StateFlow<AccountListUiState> = _accountListState.asStateFlow()

    private val _createAccountState = MutableStateFlow<CreateAccountUiState>(CreateAccountUiState.Idle)
    val createAccountState: StateFlow<CreateAccountUiState> = _createAccountState.asStateFlow()

    private val _balanceState = MutableStateFlow<BalanceUiState>(BalanceUiState.Idle)
    val balanceState: StateFlow<BalanceUiState> = _balanceState.asStateFlow()

    fun fetchAccountsByUserId(userId: Long) {
        viewModelScope.launch {
            _accountListState.value = AccountListUiState.Loading
            val result = accountRepository.getAccountsByUserId(userId)
            result.onSuccess { apiResponse ->
                _accountListState.value = AccountListUiState.Success(apiResponse.data ?: emptyList())
            }.onFailure { exception ->
                _accountListState.value = AccountListUiState.Error(exception.message ?: "Failed to load accounts")
            }
        }
    }

    fun createAccount(userId: Long, accountType: AccountType, initialDeposit: BigDecimal, currency: String = "USD") {
        viewModelScope.launch {
            _createAccountState.value = CreateAccountUiState.Loading
            val request = CreateAccountRequest(userId, accountType, initialDeposit, currency)
            val result = accountRepository.createAccount(request)
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _createAccountState.value = CreateAccountUiState.Success(apiResponse.data)
                } else {
                    _createAccountState.value = CreateAccountUiState.Error(apiResponse.message ?: "Failed to create account")
                }
            }.onFailure { exception ->
                _createAccountState.value = CreateAccountUiState.Error(exception.message ?: "Failed to create account")
            }
        }
    }

    fun fetchBalance(accountNumber: String) {
        if (accountNumber.isBlank()) {
            _balanceState.value = BalanceUiState.Error("Account number is required")
            return
        }

        viewModelScope.launch {
            _balanceState.value = BalanceUiState.Loading
            val result = accountRepository.getAccountByNumber(accountNumber)
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _balanceState.value = BalanceUiState.Success(accountNumber, apiResponse.data.balance)
                } else {
                    _balanceState.value = BalanceUiState.Error(apiResponse.message ?: "Account not found")
                }
            }.onFailure { exception ->
                _balanceState.value = BalanceUiState.Error(exception.message ?: "Failed to fetch balance")
            }
        }
    }

    fun resetCreateState() {
        _createAccountState.value = CreateAccountUiState.Idle
    }
}
