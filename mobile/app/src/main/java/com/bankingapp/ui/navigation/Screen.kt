package com.bankingapp.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object CreateAccount : Screen("create_account")
    object AccountList : Screen("account_list")
    object BalanceInquiry : Screen("balance_inquiry")
    object Deposit : Screen("deposit")
    object Withdraw : Screen("withdraw")
    object Transfer : Screen("transfer")
    object TransactionHistory : Screen("transaction_history")
}
