package com.bankingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bankingapp.data.model.DepositRequest
import com.bankingapp.data.model.TransactionResponse
import com.bankingapp.data.model.TransferRequest
import com.bankingapp.data.model.WithdrawRequest
import com.bankingapp.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

sealed interface TransactionActionUiState {
    object Idle : TransactionActionUiState
    object Loading : TransactionActionUiState
    data class Success(val transaction: TransactionResponse) : TransactionActionUiState
    data class Error(val message: String) : TransactionActionUiState
}

sealed interface TransactionHistoryUiState {
    object Idle : TransactionHistoryUiState
    object Loading : TransactionHistoryUiState
    data class Success(val transactions: List<TransactionResponse>) : TransactionHistoryUiState
    data class Error(val message: String) : TransactionHistoryUiState
}

class TransactionViewModel(
    private val transactionRepository: TransactionRepository = TransactionRepository()
) : ViewModel() {

    private val _actionState = MutableStateFlow<TransactionActionUiState>(TransactionActionUiState.Idle)
    val actionState: StateFlow<TransactionActionUiState> = _actionState.asStateFlow()

    private val _historyState = MutableStateFlow<TransactionHistoryUiState>(TransactionHistoryUiState.Idle)
    val historyState: StateFlow<TransactionHistoryUiState> = _historyState.asStateFlow()

    fun deposit(accountNumber: String, amount: BigDecimal, description: String?) {
        if (accountNumber.isBlank() || amount <= BigDecimal.ZERO) {
            _actionState.value = TransactionActionUiState.Error("Please provide valid account number and amount")
            return
        }

        viewModelScope.launch {
            _actionState.value = TransactionActionUiState.Loading
            val request = DepositRequest(accountNumber, amount, description)
            val result = transactionRepository.deposit(request)
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _actionState.value = TransactionActionUiState.Success(apiResponse.data)
                } else {
                    _actionState.value = TransactionActionUiState.Error(apiResponse.message ?: "Deposit failed")
                }
            }.onFailure { exception ->
                _actionState.value = TransactionActionUiState.Error(exception.message ?: "Deposit failed")
            }
        }
    }

    fun withdraw(accountNumber: String, amount: BigDecimal, description: String?) {
        if (accountNumber.isBlank() || amount <= BigDecimal.ZERO) {
            _actionState.value = TransactionActionUiState.Error("Please provide valid account number and amount")
            return
        }

        viewModelScope.launch {
            _actionState.value = TransactionActionUiState.Loading
            val request = WithdrawRequest(accountNumber, amount, description)
            val result = transactionRepository.withdraw(request)
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _actionState.value = TransactionActionUiState.Success(apiResponse.data)
                } else {
                    _actionState.value = TransactionActionUiState.Error(apiResponse.message ?: "Withdrawal failed")
                }
            }.onFailure { exception ->
                _actionState.value = TransactionActionUiState.Error(exception.message ?: "Withdrawal failed")
            }
        }
    }

    fun transfer(sourceAccountNumber: String, targetAccountNumber: String, amount: BigDecimal, description: String?) {
        if (sourceAccountNumber.isBlank() || targetAccountNumber.isBlank() || amount <= BigDecimal.ZERO) {
            _actionState.value = TransactionActionUiState.Error("Please fill all transfer fields correctly")
            return
        }

        viewModelScope.launch {
            _actionState.value = TransactionActionUiState.Loading
            val request = TransferRequest(sourceAccountNumber, targetAccountNumber, amount, description)
            val result = transactionRepository.transfer(request)
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _actionState.value = TransactionActionUiState.Success(apiResponse.data)
                } else {
                    _actionState.value = TransactionActionUiState.Error(apiResponse.message ?: "Transfer failed")
                }
            }.onFailure { exception ->
                _actionState.value = TransactionActionUiState.Error(exception.message ?: "Transfer failed")
            }
        }
    }

    fun fetchTransactionHistory(accountNumber: String) {
        if (accountNumber.isBlank()) {
            _historyState.value = TransactionHistoryUiState.Error("Account number is required")
            return
        }

        viewModelScope.launch {
            _historyState.value = TransactionHistoryUiState.Loading
            val result = transactionRepository.getTransactionHistory(accountNumber)
            result.onSuccess { apiResponse ->
                _historyState.value = TransactionHistoryUiState.Success(apiResponse.data ?: emptyList())
            }.onFailure { exception ->
                _historyState.value = TransactionHistoryUiState.Error(exception.message ?: "Failed to fetch transactions")
            }
        }
    }

    fun resetActionState() {
        _actionState.value = TransactionActionUiState.Idle
    }
}
