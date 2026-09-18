package com.bankingapp.data.repository

import com.bankingapp.data.model.ApiResponse
import com.bankingapp.data.model.AuthResponse
import com.bankingapp.data.model.CreateUserRequest
import com.bankingapp.data.model.LoginRequest
import com.bankingapp.data.model.UserResponse
import com.bankingapp.data.remote.AuthApiService
import com.bankingapp.data.remote.RetrofitClient

class AuthRepository(
    private val authApiService: AuthApiService = RetrofitClient.authApiService
) {

    suspend fun login(request: LoginRequest): Result<ApiResponse<AuthResponse>> {
        return try {
            val response = authApiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(request: CreateUserRequest): Result<ApiResponse<UserResponse>> {
        return try {
            val response = authApiService.createUser(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "User registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Long): Result<ApiResponse<UserResponse>> {
        return try {
            val response = authApiService.getUserById(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
