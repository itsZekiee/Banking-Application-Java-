package com.bankingapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bankingapp.ui.components.BankingTextField
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.LoadingIndicator
import com.bankingapp.ui.components.PrimaryButton
import com.bankingapp.ui.components.SectionHeader
import com.bankingapp.ui.theme.PrimaryBlue
import com.bankingapp.ui.theme.TextSecondary
import com.bankingapp.viewmodel.AccountViewModel
import com.bankingapp.viewmodel.BalanceUiState

@Composable
fun BalanceInquiryScreen(
    initialAccountNumber: String?,
    viewModel: AccountViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var accountNumber by remember { mutableStateOf(initialAccountNumber ?: "") }
    val balanceState by viewModel.balanceState.collectAsState()

    LaunchedEffect(initialAccountNumber) {
        if (!initialAccountNumber.isNullOrBlank()) {
            viewModel.fetchBalance(initialAccountNumber)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        SectionHeader(
            title = "Balance Inquiry",
            subtitle = "Check available balance for any account"
        )
        Spacer(modifier = Modifier.height(24.dp))

        BankingTextField(
            value = accountNumber,
            onValueChange = { accountNumber = it },
            label = "Account Number",
            placeholder = "Enter 10-digit account number"
        )
        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Check Balance",
            onClick = { viewModel.fetchBalance(accountNumber) }
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (val state = balanceState) {
            is BalanceUiState.Loading -> LoadingIndicator()
            is BalanceUiState.Error -> ErrorCard(message = state.message)
            is BalanceUiState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Account: ${state.accountNumber}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "$${state.balance}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                }
            }
            else -> {}
        }
    }
}
