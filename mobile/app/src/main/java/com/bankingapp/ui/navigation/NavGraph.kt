package com.bankingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bankingapp.ui.screens.AccountListScreen
import com.bankingapp.ui.screens.BalanceInquiryScreen
import com.bankingapp.ui.screens.CreateAccountScreen
import com.bankingapp.ui.screens.DepositScreen
import com.bankingapp.ui.screens.LoginScreen
import com.bankingapp.ui.screens.TransactionHistoryScreen
import com.bankingapp.ui.screens.TransferScreen
import com.bankingapp.ui.screens.WithdrawScreen
import com.bankingapp.viewmodel.AccountViewModel
import com.bankingapp.viewmodel.AuthViewModel
import com.bankingapp.viewmodel.TransactionViewModel

@Composable
fun BankingNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    accountViewModel: AccountViewModel = viewModel(),
    transactionViewModel: TransactionViewModel = viewModel()
) {
    var currentUserId by remember { mutableStateOf(1L) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { userId ->
                    currentUserId = userId
                    navController.navigate(Screen.AccountList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.CreateAccount.route)
                }
            )
        }

        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(
                userId = currentUserId,
                viewModel = accountViewModel,
                onAccountCreated = {
                    navController.navigate(Screen.AccountList.route) {
                        popUpTo(Screen.CreateAccount.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AccountList.route) {
            AccountListScreen(
                userId = currentUserId,
                viewModel = accountViewModel,
                onSelectAccount = { accountNumber ->
                    navController.navigate("${Screen.BalanceInquiry.route}?acc=$accountNumber")
                },
                onNavigateToCreateAccount = {
                    navController.navigate(Screen.CreateAccount.route)
                },
                onNavigateToDeposit = { accountNumber ->
                    navController.navigate("${Screen.Deposit.route}?acc=$accountNumber")
                },
                onNavigateToWithdraw = { accountNumber ->
                    navController.navigate("${Screen.Withdraw.route}?acc=$accountNumber")
                },
                onNavigateToTransfer = { accountNumber ->
                    navController.navigate("${Screen.Transfer.route}?acc=$accountNumber")
                },
                onNavigateToHistory = { accountNumber ->
                    navController.navigate("${Screen.TransactionHistory.route}?acc=$accountNumber")
                }
            )
        }

        composable(
            route = "${Screen.BalanceInquiry.route}?acc={acc}",
            arguments = listOf(navArgument("acc") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val acc = backStackEntry.arguments?.getString("acc")
            BalanceInquiryScreen(
                initialAccountNumber = acc,
                viewModel = accountViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Screen.Deposit.route}?acc={acc}",
            arguments = listOf(navArgument("acc") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val acc = backStackEntry.arguments?.getString("acc")
            DepositScreen(
                initialAccountNumber = acc,
                viewModel = transactionViewModel,
                onSuccess = {
                    navController.navigate(Screen.AccountList.route) {
                        popUpTo(Screen.AccountList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.Withdraw.route}?acc={acc}",
            arguments = listOf(navArgument("acc") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val acc = backStackEntry.arguments?.getString("acc")
            WithdrawScreen(
                initialAccountNumber = acc,
                viewModel = transactionViewModel,
                onSuccess = {
                    navController.navigate(Screen.AccountList.route) {
                        popUpTo(Screen.AccountList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.Transfer.route}?acc={acc}",
            arguments = listOf(navArgument("acc") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val acc = backStackEntry.arguments?.getString("acc")
            TransferScreen(
                initialSourceAccountNumber = acc,
                viewModel = transactionViewModel,
                onSuccess = {
                    navController.navigate(Screen.AccountList.route) {
                        popUpTo(Screen.AccountList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Screen.TransactionHistory.route}?acc={acc}",
            arguments = listOf(navArgument("acc") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val acc = backStackEntry.arguments?.getString("acc") ?: ""
            TransactionHistoryScreen(
                accountNumber = acc,
                viewModel = transactionViewModel
            )
        }
    }
}
