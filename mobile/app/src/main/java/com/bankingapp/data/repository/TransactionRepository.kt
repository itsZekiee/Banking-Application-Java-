package com.bankingapp.data.repository

import com.bankingapp.data.model.ApiResponse
import com.bankingapp.data.model.DepositRequest
import com.bankingapp.data.model.TransactionResponse
import com.bankingapp.data.model.TransferRequest
import com.bankingapp.data.model.WithdrawRequest
import com.bankingapp.data.remote.RetrofitClient
import com.bankingapp.data.remote.TransactionApiService

class TransactionRepository(
    private val transactionApiService: TransactionApiService = RetrofitClient.transactionApiService
) {

    suspend fun deposit(request: DepositRequest): Result<ApiResponse<TransactionResponse>> {
        return try {
            val response = transactionApiService.deposit(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Deposit failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun withdraw(request: WithdrawRequest): Result<ApiResponse<TransactionResponse>> {
        return try {
            val response = transactionApiService.withdraw(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Withdrawal failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun transfer(request: TransferRequest): Result<ApiResponse<TransactionResponse>> {
        return try {
            val response = transactionApiService.transfer(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Transfer failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTransactionHistory(accountNumber: String): Result<ApiResponse<List<TransactionResponse>>> {
        return try {
            val response = transactionApiService.getTransactionHistory(accountNumber)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch transaction history"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTransactionByReference(reference: String): Result<ApiResponse<TransactionResponse>> {
        return try {
            val response = transactionApiService.getTransactionByReference(reference)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch transaction"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
