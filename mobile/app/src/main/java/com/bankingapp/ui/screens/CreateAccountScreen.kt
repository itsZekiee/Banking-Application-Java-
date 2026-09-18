package com.bankingapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bankingapp.data.model.AccountType
import com.bankingapp.ui.components.BankingTextField
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.PrimaryButton
import com.bankingapp.ui.components.SectionHeader
import com.bankingapp.viewmodel.AccountViewModel
import com.bankingapp.viewmodel.CreateAccountUiState
import java.math.BigDecimal

@Composable
fun CreateAccountScreen(
    userId: Long,
    viewModel: AccountViewModel,
    onAccountCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val createState by viewModel.createAccountState.collectAsState()

    var selectedType by remember { mutableStateOf(AccountType.SAVINGS) }
    var initialDepositStr by remember { mutableStateOf("100.00") }

    LaunchedEffect(createState) {
        if (createState is CreateAccountUiState.Success) {
            viewModel.resetCreateState()
            onAccountCreated()
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
            title = "Open New Account",
            subtitle = "Select your account type and make an initial deposit"
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (createState is CreateAccountUiState.Error) {
            ErrorCard(message = (createState as CreateAccountUiState.Error).message)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Account Type", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        AccountType.values().forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (type == selectedType),
                        onClick = { selectedType = type }
                    )
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (type == selectedType),
                    onClick = { selectedType = type }
                )
                Text(
                    text = type.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BankingTextField(
            value = initialDepositStr,
            onValueChange = { initialDepositStr = it },
            label = "Initial Deposit Amount ($)",
            placeholder = "0.00",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Create Account",
            onClick = {
                val deposit = initialDepositStr.toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.createAccount(userId, selectedType, deposit)
            },
            isLoading = createState is CreateAccountUiState.Loading
        )
    }
}
