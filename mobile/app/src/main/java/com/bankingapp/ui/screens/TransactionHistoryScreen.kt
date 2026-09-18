package com.bankingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bankingapp.data.model.TransactionResponse
import com.bankingapp.data.model.TransactionType
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.LoadingIndicator
import com.bankingapp.ui.components.SectionHeader
import com.bankingapp.ui.theme.ErrorRed
import com.bankingapp.ui.theme.SecondaryGreen
import com.bankingapp.ui.theme.TextSecondary
import com.bankingapp.viewmodel.TransactionHistoryUiState
import com.bankingapp.viewmodel.TransactionViewModel

@Composable
fun TransactionHistoryScreen(
    accountNumber: String,
    viewModel: TransactionViewModel,
    modifier: Modifier = Modifier
) {
    val historyState by viewModel.historyState.collectAsState()

    LaunchedEffect(accountNumber) {
        viewModel.fetchTransactionHistory(accountNumber)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "Transaction History",
            subtitle = "Recent activity for account: $accountNumber"
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = historyState) {
            is TransactionHistoryUiState.Loading -> LoadingIndicator()
            is TransactionHistoryUiState.Error -> ErrorCard(message = state.message)
            is TransactionHistoryUiState.Success -> {
                if (state.transactions.isEmpty()) {
                    Text(
                        text = "No transactions found for this account.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.transactions) { transaction ->
                            TransactionRowItem(transaction = transaction, currentAccount = accountNumber)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun TransactionRowItem(
    transaction: TransactionResponse,
    currentAccount: String
) {
    val isCredit = transaction.transactionType == TransactionType.DEPOSIT ||
            (transaction.transactionType == TransactionType.TRANSFER && transaction.targetAccountNumber == currentAccount)

    val amountColor = if (isCredit) SecondaryGreen else ErrorRed
    val prefix = if (isCredit) "+" else "-"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.transactionType.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = transaction.description ?: "Ref: ${transaction.transactionReference}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                if (transaction.timestamp != null) {
                    Text(
                        text = transaction.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            Text(
                text = "$prefix$${transaction.amount}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}
