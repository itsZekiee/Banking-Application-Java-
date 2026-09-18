package com.bankingapp.data.repository

import com.bankingapp.data.model.AccountResponse
import com.bankingapp.data.model.ApiResponse
import com.bankingapp.data.model.CreateAccountRequest
import com.bankingapp.data.remote.AccountApiService
import com.bankingapp.data.remote.RetrofitClient

class AccountRepository(
    private val accountApiService: AccountApiService = RetrofitClient.accountApiService
) {

    suspend fun createAccount(request: CreateAccountRequest): Result<ApiResponse<AccountResponse>> {
        return try {
            val response = accountApiService.createAccount(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create account"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAccountByNumber(accountNumber: String): Result<ApiResponse<AccountResponse>> {
        return try {
            val response = accountApiService.getAccountByNumber(accountNumber)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch account"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAccountsByUserId(userId: Long): Result<ApiResponse<List<AccountResponse>>> {
        return try {
            val response = accountApiService.getAccountsByUserId(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch user accounts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBalance(accountNumber: String): Result<ApiResponse<Map<String, Any>>> {
        return try {
            val response = accountApiService.getBalance(accountNumber)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch balance"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
