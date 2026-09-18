package com.bankingapp.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bankingapp.data.model.AccountResponse
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.LoadingIndicator
import com.bankingapp.ui.components.PrimaryButton
import com.bankingapp.ui.components.SectionHeader
import com.bankingapp.ui.theme.PrimaryBlue
import com.bankingapp.ui.theme.SecondaryGreen
import com.bankingapp.ui.theme.TextSecondary
import com.bankingapp.viewmodel.AccountListUiState
import com.bankingapp.viewmodel.AccountViewModel

@Composable
fun AccountListScreen(
    userId: Long,
    viewModel: AccountViewModel,
    onSelectAccount: (String) -> Unit,
    onNavigateToCreateAccount: () -> Unit,
    onNavigateToDeposit: (String) -> Unit,
    onNavigateToWithdraw: (String) -> Unit,
    onNavigateToTransfer: (String) -> Unit,
    onNavigateToHistory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState by viewModel.accountListState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchAccountsByUserId(userId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "My Accounts",
            subtitle = "Manage your accounts and quick operations"
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = listState) {
            is AccountListUiState.Loading -> LoadingIndicator()
            is AccountListUiState.Error -> ErrorCard(message = state.message)
            is AccountListUiState.Success -> {
                if (state.accounts.isEmpty()) {
                    Text(
                        text = "No accounts found. Create your first account!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.accounts) { account ->
                            AccountCardItem(
                                account = account,
                                onClick = { onSelectAccount(account.accountNumber) },
                                onDeposit = { onNavigateToDeposit(account.accountNumber) },
                                onWithdraw = { onNavigateToWithdraw(account.accountNumber) },
                                onTransfer = { onNavigateToTransfer(account.accountNumber) },
                                onHistory = { onNavigateToHistory(account.accountNumber) }
                            )
                        }
                    }
                }
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "+ Open Another Account",
            onClick = onNavigateToCreateAccount
        )
    }
}

@Composable
private fun AccountCardItem(
    account: AccountResponse,
    onClick: () -> Unit,
    onDeposit: () -> Unit,
    onWithdraw: () -> Unit,
    onTransfer: () -> Unit,
    onHistory: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${account.accountType} Account",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryBlue
                )
                Text(
                    text = account.status.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = SecondaryGreen
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Acc: ${account.accountNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${account.currency} ${account.balance}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(onClick = onDeposit, modifier = Modifier.weight(1f)) {
                    Text("Deposit")
                }
                OutlinedButton(onClick = onWithdraw, modifier = Modifier.weight(1f)) {
                    Text("Withdraw")
                }
                OutlinedButton(onClick = onTransfer, modifier = Modifier.weight(1f)) {
                    Text("Transfer")
                }
                OutlinedButton(onClick = onHistory, modifier = Modifier.weight(1f)) {
                    Text("History")
                }
            }
        }
    }
}
