package com.bankingapp.data.model

import java.math.BigDecimal

enum class AccountType {
    SAVINGS,
    CHECKING,
    INVESTMENT
}

enum class AccountStatus {
    ACTIVE,
    FROZEN,
    CLOSED
}

data class CreateAccountRequest(
    val userId: Long,
    val accountType: AccountType,
    val initialDeposit: BigDecimal = BigDecimal.ZERO,
    val currency: String = "USD"
)

data class AccountResponse(
    val id: Long,
    val accountNumber: String,
    val accountType: AccountType,
    val balance: BigDecimal,
    val currency: String,
    val status: AccountStatus,
    val userId: Long?,
    val userFullName: String?,
    val createdAt: String?
)

data class BalanceResponse(
    val accountNumber: String,
    val balance: BigDecimal
)
