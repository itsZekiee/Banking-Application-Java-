package com.bankingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bankingapp.data.model.AuthResponse
import com.bankingapp.data.model.CreateUserRequest
import com.bankingapp.data.model.LoginRequest
import com.bankingapp.data.model.UserResponse
import com.bankingapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Authenticated(val auth: AuthResponse) : AuthUiState
    data class UserCreated(val user: UserResponse) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(usernameOrEmail: String, password: String) {
        if (usernameOrEmail.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Please provide both username and password")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(LoginRequest(usernameOrEmail, password))
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _uiState.value = AuthUiState.Authenticated(apiResponse.data)
                } else {
                    _uiState.value = AuthUiState.Error(apiResponse.message ?: "Authentication failed")
                }
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "An unexpected error occurred")
            }
        }
    }

    fun registerUser(username: String, email: String, password: String, fullName: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank() || fullName.isBlank()) {
            _uiState.value = AuthUiState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.createUser(CreateUserRequest(username, email, password, fullName))
            result.onSuccess { apiResponse ->
                if (apiResponse.data != null) {
                    _uiState.value = AuthUiState.UserCreated(apiResponse.data)
                } else {
                    _uiState.value = AuthUiState.Error(apiResponse.message ?: "Registration failed")
                }
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "An unexpected error occurred")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
