package com.bankingapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.PiggyLogo
import com.bankingapp.ui.theme.BankingAppTheme
import com.bankingapp.ui.theme.BorderGray
import com.bankingapp.ui.theme.BrandGreen
import com.bankingapp.ui.theme.BrandGreenBright
import com.bankingapp.ui.theme.BrandGreenSoft
import com.bankingapp.ui.theme.FieldGray
import com.bankingapp.ui.theme.SurfaceLight
import com.bankingapp.ui.theme.TextInk
import com.bankingapp.ui.theme.TextMuted
import com.bankingapp.viewmodel.AuthUiState
import com.bankingapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (Long) -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    onForgotPassword: () -> Unit = {},
    onBiometricSignIn: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Authenticated) {
            val userId = (uiState as AuthUiState.Authenticated).auth.userId
            onLoginSuccess(userId)
        }
    }

    SignInContent(
        isLoading = uiState is AuthUiState.Loading,
        errorMessage = (uiState as? AuthUiState.Error)?.message,
        onSignIn = { email, password -> viewModel.login(email, password) },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPassword = onForgotPassword,
        onBiometricSignIn = onBiometricSignIn,
        modifier = modifier
    )
}

@Composable
private fun SignInContent(
    isLoading: Boolean,
    errorMessage: String?,
    onSignIn: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onBiometricSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(120)
        contentVisible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        SignInBackdrop(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(650)) + slideInVertically(tween(650)) { -it / 3 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PiggyLogo(size = 112.dp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.displaySmall,
                        color = TextInk
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hi! Welcome back, you've been missed",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(700)) + slideInVertically(tween(700)) { it / 4 }
            ) {
                Column {
                    if (!errorMessage.isNullOrBlank()) {
                        ErrorCard(message = errorMessage)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    FieldLabel(text = "Email")
                    Spacer(modifier = Modifier.height(8.dp))
                    SoftTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "example@gmail.com",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    FieldLabel(text = "Password")
                    Spacer(modifier = Modifier.height(8.dp))
                    SoftTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Enter your password",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Filled.Visibility
                                    } else {
                                        Icons.Filled.VisibilityOff
                                    },
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = TextMuted
                                )
                            }
                        }
                    )

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        TextButton(onClick = onForgotPassword) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextInk,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onSignIn(email, password) },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandGreen,
                            contentColor = SurfaceLight
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = SurfaceLight,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Sign In",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = BorderGray)
                        Text(
                            text = "or quick sign in",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Divider(modifier = Modifier.weight(1f), color = BorderGray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedButton(
                        onClick = onBiometricSignIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderGray),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceLight,
                            contentColor = TextInk
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Fingerprint,
                            contentDescription = null,
                            tint = BrandGreenBright,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sign in with Biometrics",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(44.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextInk
                )
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextInk,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable(onClick = onNavigateToRegister)
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            SecurityFootnote(text = "FDIC-Insured Partner • 256-Bit Bank Encryption")
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = TextInk,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SoftTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted
            )
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldGray,
            unfocusedContainerColor = FieldGray,
            disabledContainerColor = FieldGray,
            focusedBorderColor = BrandGreenBright,
            unfocusedBorderColor = FieldGray,
            cursorColor = BrandGreen
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
    )
}

/**
 * Decorative organic mint blobs mirroring the shapes of the Sign In design.
 */
@Composable
private fun SignInBackdrop(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val topBlob = Path().apply {
            moveTo(w * 0.06f, 0f)
            cubicTo(w * -0.10f, h * 0.12f, w * 0.02f, h * 0.28f, w * 0.34f, h * 0.31f)
            cubicTo(w * 0.70f, h * 0.34f, w * 1.05f, h * 0.24f, w * 1.02f, h * -0.02f)
            close()
        }
        drawPath(path = topBlob, color = BrandGreenSoft.copy(alpha = 0.50f))

        drawOval(
            color = BrandGreenSoft.copy(alpha = 0.30f),
            topLeft = Offset(w * 0.16f, -h * 0.05f),
            size = Size(w * 0.95f, h * 0.30f)
        )

        val bottomBlob = Path().apply {
            moveTo(0f, h * 0.74f)
            cubicTo(w * 0.30f, h * 0.74f, w * 0.54f, h * 0.88f, w * 0.46f, h)
            lineTo(0f, h)
            close()
        }
        drawPath(path = bottomBlob, color = BrandGreenSoft.copy(alpha = 0.45f))

        val bottomBlobInner = Path().apply {
            moveTo(0f, h * 0.84f)
            cubicTo(w * 0.18f, h * 0.84f, w * 0.34f, h * 0.93f, w * 0.28f, h)
            lineTo(0f, h)
            close()
        }
        drawPath(path = bottomBlobInner, color = BrandGreenSoft.copy(alpha = 0.55f))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SignInContentPreview() {
    BankingAppTheme {
        SignInContent(
            isLoading = false,
            errorMessage = null,
            onSignIn = { _, _ -> },
            onNavigateToRegister = {},
            onForgotPassword = {},
            onBiometricSignIn = {}
        )
    }
}
