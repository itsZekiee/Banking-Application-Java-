package com.bankingapp.data.remote

import com.bankingapp.data.model.ApiResponse
import com.bankingapp.data.model.AuthResponse
import com.bankingapp.data.model.CreateUserRequest
import com.bankingapp.data.model.LoginRequest
import com.bankingapp.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST("api/v1/users")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): Response<ApiResponse<UserResponse>>

    @GET("api/v1/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): Response<ApiResponse<UserResponse>>

    @GET("api/v1/users/username/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String
    ): Response<ApiResponse<UserResponse>>
}
