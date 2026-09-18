package com.bankingapp.data.remote

import com.bankingapp.data.model.ApiResponse
import com.bankingapp.data.model.DepositRequest
import com.bankingapp.data.model.TransactionResponse
import com.bankingapp.data.model.TransferRequest
import com.bankingapp.data.model.WithdrawRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionApiService {

    @POST("api/v1/transactions/deposit")
    suspend fun deposit(
        @Body request: DepositRequest
    ): Response<ApiResponse<TransactionResponse>>

    @POST("api/v1/transactions/withdraw")
    suspend fun withdraw(
        @Body request: WithdrawRequest
    ): Response<ApiResponse<TransactionResponse>>

    @POST("api/v1/transactions/transfer")
    suspend fun transfer(
        @Body request: TransferRequest
    ): Response<ApiResponse<TransactionResponse>>

    @GET("api/v1/transactions/account/{accountNumber}")
    suspend fun getTransactionHistory(
        @Path("accountNumber") accountNumber: String
    ): Response<ApiResponse<List<TransactionResponse>>>

    @GET("api/v1/transactions/{reference}")
    suspend fun getTransactionByReference(
        @Path("reference") reference: String
    ): Response<ApiResponse<TransactionResponse>>
}
