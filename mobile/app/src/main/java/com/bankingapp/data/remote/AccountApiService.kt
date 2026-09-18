package com.bankingapp.data.remote

import com.bankingapp.data.model.AccountResponse
import com.bankingapp.data.model.ApiResponse
import com.bankingapp.data.model.CreateAccountRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApiService {

    @POST("api/v1/accounts")
    suspend fun createAccount(
        @Body request: CreateAccountRequest
    ): Response<ApiResponse<AccountResponse>>

    @GET("api/v1/accounts/{accountNumber}")
    suspend fun getAccountByNumber(
        @Path("accountNumber") accountNumber: String
    ): Response<ApiResponse<AccountResponse>>

    @GET("api/v1/accounts/user/{userId}")
    suspend fun getAccountsByUserId(
        @Path("userId") userId: Long
    ): Response<ApiResponse<List<AccountResponse>>>

    @GET("api/v1/accounts/{accountNumber}/balance")
    suspend fun getBalance(
        @Path("accountNumber") accountNumber: String
    ): Response<ApiResponse<Map<String, Any>>>
}
