package com.bankingapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bankingapp.ui.components.BankingTextField
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.PrimaryButton
import com.bankingapp.ui.components.SectionHeader
import com.bankingapp.viewmodel.AuthUiState
import com.bankingapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (Long) -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Authenticated) {
            val userId = (uiState as AuthUiState.Authenticated).auth.userId
            onLoginSuccess(userId)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        SectionHeader(
            title = "Welcome Back",
            subtitle = "Sign in to access your banking account"
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (uiState is AuthUiState.Error) {
            ErrorCard(message = (uiState as AuthUiState.Error).message)
            Spacer(modifier = Modifier.height(16.dp))
        }

        BankingTextField(
            value = usernameOrEmail,
            onValueChange = { usernameOrEmail = it },
            label = "Username or Email",
            placeholder = "Enter your username or email"
        )
        Spacer(modifier = Modifier.height(16.dp))

        BankingTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Enter your password",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Sign In",
            onClick = { viewModel.login(usernameOrEmail, password) },
            isLoading = uiState is AuthUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Open a new account")
        }
    }
}
