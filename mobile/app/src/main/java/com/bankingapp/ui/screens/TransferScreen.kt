package com.bankingapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bankingapp.ui.components.BankingTextField
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.PrimaryButton
import com.bankingapp.ui.components.SectionHeader
import com.bankingapp.viewmodel.TransactionActionUiState
import com.bankingapp.viewmodel.TransactionViewModel
import java.math.BigDecimal

@Composable
fun TransferScreen(
    initialSourceAccountNumber: String?,
    viewModel: TransactionViewModel,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var sourceAccountNumber by remember { mutableStateOf(initialSourceAccountNumber ?: "") }
    var targetAccountNumber by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val actionState by viewModel.actionState.collectAsState()

    LaunchedEffect(actionState) {
        if (actionState is TransactionActionUiState.Success) {
            viewModel.resetActionState()
            onSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        SectionHeader(
            title = "Transfer Funds",
            subtitle = "Send money to another bank account instantly"
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (actionState is TransactionActionUiState.Error) {
            ErrorCard(message = (actionState as TransactionActionUiState.Error).message)
            Spacer(modifier = Modifier.height(16.dp))
        }

        BankingTextField(
            value = sourceAccountNumber,
            onValueChange = { sourceAccountNumber = it },
            label = "From Account Number",
            placeholder = "Source account number"
        )
        Spacer(modifier = Modifier.height(16.dp))

        BankingTextField(
            value = targetAccountNumber,
            onValueChange = { targetAccountNumber = it },
            label = "To Account Number",
            placeholder = "Destination account number"
        )
        Spacer(modifier = Modifier.height(16.dp))

        BankingTextField(
            value = amountStr,
            onValueChange = { amountStr = it },
            label = "Transfer Amount ($)",
            placeholder = "0.00",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(16.dp))

        BankingTextField(
            value = description,
            onValueChange = { description = it },
            label = "Description / Reference",
            placeholder = "e.g. Invoice payment"
        )
        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Confirm Transfer",
            onClick = {
                val amount = amountStr.toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.transfer(sourceAccountNumber, targetAccountNumber, amount, description)
            },
            isLoading = actionState is TransactionActionUiState.Loading
        )
    }
}
