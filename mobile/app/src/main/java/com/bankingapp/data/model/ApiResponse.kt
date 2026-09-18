package com.bankingapp.data.model

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val timestamp: String?
)

data class ErrorResponse(
    val status: Int,
    val error: String?,
    val message: String?,
    val details: List<String>?,
    val timestamp: String?
)
