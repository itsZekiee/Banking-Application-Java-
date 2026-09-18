package com.bankingapp.data.model

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String
)

data class AuthResponse(
    val token: String,
    val tokenType: String,
    val userId: Long,
    val username: String,
    val email: String
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val createdAt: String?
)
