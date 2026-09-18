package com.bankingapp.data.model

import java.math.BigDecimal

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}

enum class TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED
}

data class DepositRequest(
    val accountNumber: String,
    val amount: BigDecimal,
    val description: String? = null
)

data class WithdrawRequest(
    val accountNumber: String,
    val amount: BigDecimal,
    val description: String? = null
)

data class TransferRequest(
    val sourceAccountNumber: String,
    val targetAccountNumber: String,
    val amount: BigDecimal,
    val description: String? = null
)

data class TransactionResponse(
    val id: Long,
    val transactionReference: String,
    val sourceAccountNumber: String?,
    val targetAccountNumber: String?,
    val amount: BigDecimal,
    val transactionType: TransactionType,
    val status: TransactionStatus,
    val description: String?,
    val timestamp: String?
)
