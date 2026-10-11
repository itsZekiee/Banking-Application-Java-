package com.bankingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bankingapp.data.model.AccountType
import com.bankingapp.ui.theme.BorderGray
import com.bankingapp.ui.theme.BrandGreen
import com.bankingapp.ui.theme.BrandGreenMist
import com.bankingapp.ui.theme.BrandGreenSoft
import com.bankingapp.ui.theme.BrandGreenWash
import com.bankingapp.ui.theme.FieldGray
import com.bankingapp.ui.theme.TextInk
import com.bankingapp.ui.theme.TextMuted
import com.bankingapp.ui.theme.TextSecondary
import com.bankingapp.viewmodel.AccountViewModel
import com.bankingapp.viewmodel.CreateAccountUiState
import java.math.BigDecimal

@Composable
fun CreateAccountScreen(
    userId: Long,
    viewModel: AccountViewModel,
    onAccountCreated: () -> Unit,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val createState by viewModel.createAccountState.collectAsState()
    var step by remember { mutableStateOf(1) }
    var selectedType by remember { mutableStateOf(AccountType.CHECKING) }
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var birthPlace by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var employmentUploaded by remember { mutableStateOf(false) }
    var idUploaded by remember { mutableStateOf(false) }
    var showDetailsErrors by remember { mutableStateOf(false) }
    var showDocumentErrors by remember { mutableStateOf(false) }

    LaunchedEffect(createState) {
        if (createState is CreateAccountUiState.Success) {
            viewModel.resetCreateState()
            onAccountCreated()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGreenWash)
    ) {
        Header(
            step = step,
            onBack = { if (step > 1) step-- else onBack() },
            onReset = {
                step = 1
                selectedType = AccountType.CHECKING
                employmentUploaded = false
                idUploaded = false
                showDetailsErrors = false
                showDocumentErrors = false
            }
        )
        ProgressHeader(step = step)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            when (step) {
                1 -> ProductStep(
                    selectedType = selectedType,
                    onSelect = { selectedType = it }
                )
                2 -> DetailsStep(
                    firstName = firstName,
                    middleName = middleName,
                    lastName = lastName,
                    birthday = birthday,
                    birthPlace = birthPlace,
                    address = address,
                    phone = phone,
                    onFirstName = { firstName = it },
                    onMiddleName = { middleName = it },
                    onLastName = { lastName = it },
                    onBirthday = { birthday = it },
                    onBirthPlace = { birthPlace = it },
                    onAddress = { address = it },
                    onPhone = { phone = it },
                    showErrors = showDetailsErrors
                )
                else -> DocumentsStep(
                    employmentUploaded = employmentUploaded,
                    idUploaded = idUploaded,
                    onEmploymentUploaded = { employmentUploaded = true },
                    onIdUploaded = { idUploaded = true },
                    showErrors = showDocumentErrors
                )
            }
            if (createState is CreateAccountUiState.Error) {
                Text(
                    text = (createState as CreateAccountUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
        BottomActions(
            step = step,
            loading = createState is CreateAccountUiState.Loading,
            onBack = { if (step > 1) step-- else onBack() },
            onContinue = {
                when (step) {
                    1 -> step = 2
                    2 -> {
                        showDetailsErrors = listOf(
                            firstName, middleName, lastName, birthday, birthPlace, address, phone
                        ).any(String::isBlank)
                        if (!showDetailsErrors) step = 3
                    }
                    else -> {
                        showDocumentErrors = !employmentUploaded || !idUploaded
                        if (!showDocumentErrors) {
                            viewModel.createAccount(
                                userId = userId,
                                accountType = selectedType,
                                initialDeposit = BigDecimal.ZERO
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun Header(step: Int, onBack: () -> Unit, onReset: () -> Unit) {
    Surface(color = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextSecondary,
                modifier = Modifier.size(30.dp).clickable(onClick = onBack)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                Text("Open an Account", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextInk)
                Text(
                    text = when (step) {
                        1 -> "Phase 1: Product Selection"
                        2 -> "Phase 2: Personal Details"
                        else -> "Phase 3: Required Documents"
                    },
                    color = BrandGreen,
                    fontSize = 14.sp
                )
            }
            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreen)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Reset")
            }
        }
    }
}

@Composable
private fun ProgressHeader(step: Int) {
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            ProgressNode("Account Type", 1, step >= 1, step > 1, Modifier.weight(1f))
            ProgressConnector(completed = step > 1, modifier = Modifier.weight(0.55f))
            ProgressNode("Credentials", 2, step >= 2, step > 2, Modifier.weight(1f))
            ProgressConnector(completed = step > 2, modifier = Modifier.weight(0.55f))
            ProgressNode("Documents", 3, step >= 3, false, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProgressConnector(completed: Boolean, modifier: Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(top = 20.dp),
        thickness = 3.dp,
        color = if (completed) BrandGreen else BorderGray
    )
}

@Composable
private fun ProgressNode(label: String, number: Int, active: Boolean, complete: Boolean, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(40.dp).background(if (active) BrandGreen else Color.White, CircleShape)
                .border(2.dp, if (active) BrandGreen else BorderGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (complete) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White)
            else Text("$number", color = if (active) Color.White else TextMuted, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(label, color = if (active) BrandGreen else TextMuted, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, maxLines = 1)
    }
}

@Composable
private fun ProductStep(selectedType: AccountType, onSelect: (AccountType) -> Unit) {
    StepIntro("Step 1 of 3", "Choose your account type", "Select the everyday banking solution that matches your current lifestyle and financial needs.")
    AccountOption(
        title = "Debit Account",
        subtitle = "Everyday Use",
        description = "Spend what you have. Zero monthly maintenance fees, instant virtual debit card activation, and ATM access nationwide.",
        selected = selectedType == AccountType.CHECKING,
        onClick = { onSelect(AccountType.CHECKING) }
    )
    AccountOption(
        title = "Credit Account",
        subtitle = "Flexible Limit",
        description = "Borrow up to a flexible credit limit. Earn reward points on dining and travel purchases with up to 45 days interest-free grace period.",
        selected = selectedType == AccountType.SAVINGS,
        onClick = { onSelect(AccountType.SAVINGS) }
    )
    Notice("Credit accounts are available to applicants 18 and older. Debit accounts can be opened independently or as a joint account for minors.")
}

@Composable
private fun AccountOption(title: String, subtitle: String, description: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 18.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(2.dp, if (selected) BrandGreen else BorderGray),
        shadowElevation = 1.dp
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(32.dp))
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextInk)
                    Text(subtitle, color = BrandGreen, fontWeight = FontWeight.SemiBold)
                }
                Box(Modifier.size(30.dp).background(if (selected) BrandGreen else Color.White, CircleShape)
                    .border(2.dp, if (selected) BrandGreen else BorderGray, CircleShape), contentAlignment = Alignment.Center) {
                    if (selected) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(description, color = TextSecondary, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.sp)
            Spacer(Modifier.height(14.dp))
            Text(if (selected) "✓ Instant digital setup & virtual card ready" else "◇ Requires proof of income and credit assessment",
                color = if (selected) BrandGreen else TextSecondary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DetailsStep(
    firstName: String, middleName: String, lastName: String, birthday: String, birthPlace: String,
    address: String, phone: String, onFirstName: (String) -> Unit, onMiddleName: (String) -> Unit,
    onLastName: (String) -> Unit, onBirthday: (String) -> Unit, onBirthPlace: (String) -> Unit,
    onAddress: (String) -> Unit, onPhone: (String) -> Unit, showErrors: Boolean
) {
    StepIntro("Step 2 of 3", "Your personal details", "Please enter your legal name and contact details as they appear on your government-issued ID.")
    Notice("Verified Encryption: Data is encrypted following BSP bank-grade privacy standards.")
    Surface(Modifier.fillMaxWidth().padding(top = 18.dp), RoundedCornerShape(22.dp), Color.White, shadowElevation = 1.dp) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FormField("First Name *", firstName, onFirstName, isError = showErrors && firstName.isBlank())
            FormField("Middle Name *", middleName, onMiddleName, isError = showErrors && middleName.isBlank())
            FormField("Last Name *", lastName, onLastName, isError = showErrors && lastName.isBlank())
            FormField("Birthday *", birthday, onBirthday, KeyboardType.Ascii, showErrors && birthday.isBlank())
            FormField("Birth Place *", birthPlace, onBirthPlace, isError = showErrors && birthPlace.isBlank())
            FormField("Address Details *", address, onAddress, isError = showErrors && address.isBlank())
            FormField("Mobile Number *", phone, onPhone, KeyboardType.Phone, showErrors && phone.isBlank())
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold, color = TextInk)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
            placeholder = { Text("Enter $label", color = TextMuted) },
            singleLine = true,
            isError = isError,
            supportingText = if (isError) ({ Text("Required") }) else null,
            shape = RoundedCornerShape(14.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(unfocusedContainerColor = FieldGray),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
private fun DocumentsStep(
    employmentUploaded: Boolean,
    idUploaded: Boolean,
    onEmploymentUploaded: () -> Unit,
    onIdUploaded: () -> Unit,
    showErrors: Boolean
) {
    StepIntro("Step 3 of 3", "Upload required documents", "Submit clear photos or PDF copies of your government-issued identity documents to complete your neo-banking account verification.")
    Notice("Verified Encryption: Documents are encrypted and securely stored in BSP / FDIC-compliant bank vault.")
    DocumentCard("PSA Birth Certificate", "Government authenticated copy", true, true, "birth_cert_psa.pdf", "1.8 MB • Verified copy", {})
    DocumentCard("Certificate of Employment (COE)", "Or latest 3 months payslip / income proof", true, employmentUploaded, null, null, onEmploymentUploaded, showErrors && !employmentUploaded)
    DocumentCard("Government Valid ID", "Passport / PhilID / Driver's License", true, idUploaded, if (idUploaded) "ph_national_id.jpg" else null, if (idUploaded) "Uploaded • 3.2 MB" else null, onIdUploaded, showErrors && !idUploaded)
    if (showErrors && (!employmentUploaded || !idUploaded)) {
        Text("Upload all required documents to continue.", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp))
    }
    Surface(Modifier.fillMaxWidth().padding(top = 18.dp), RoundedCornerShape(16.dp), BrandGreenMist) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = BrandGreen)
            Spacer(Modifier.width(10.dp))
            Text("256-Bit Bank-grade encryption. Your documents are encrypted end-to-end and stored in secure vault storage.", color = TextSecondary)
        }
    }
}

@Composable
private fun DocumentCard(
    title: String,
    subtitle: String,
    required: Boolean,
    uploaded: Boolean,
    fileName: String?,
    fileInfo: String?,
    onUpload: () -> Unit,
    isError: Boolean = false
) {
    Surface(
    Modifier.fillMaxWidth().padding(top = 16.dp),
    RoundedCornerShape(20.dp),
    Color.White,
    border = if (isError) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error) else null,
    shadowElevation = 1.dp
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Description, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(30.dp))
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(title, fontWeight = FontWeight.Bold, color = TextInk)
                    Text(subtitle, color = TextSecondary, fontSize = 14.sp)
                }
                Text(if (uploaded) "✓ Uploaded" else if (required) "REQUIRED" else "Pending", color = if (uploaded) BrandGreen else TextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            if (uploaded && fileName != null) {
                Row(Modifier.fillMaxWidth().padding(top = 14.dp).background(FieldGray, RoundedCornerShape(12.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("PDF", color = BrandGreen, fontWeight = FontWeight.Bold)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(fileName, fontWeight = FontWeight.SemiBold)
                        Text(fileInfo.orEmpty(), color = TextSecondary, fontSize = 13.sp)
                    }
                }
            } else if (!uploaded) {
                OutlinedButton(onClick = onUpload, modifier = Modifier.fillMaxWidth().padding(top = 14.dp)) {
                    Icon(Icons.Filled.UploadFile, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Browse File or Take Photo")
                }
            }
        }
    }
}

@Composable
private fun StepIntro(step: String, title: String, description: String) {
    Text(step, color = BrandGreen, fontWeight = FontWeight.Bold, modifier = Modifier.background(BrandGreenSoft, RoundedCornerShape(20.dp)).padding(horizontal = 14.dp, vertical = 8.dp))
    Spacer(Modifier.height(18.dp))
    Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextInk)
    Spacer(Modifier.height(8.dp))
    Text(description, color = TextSecondary, style = MaterialTheme.typography.bodyLarge, lineHeight = 25.sp)
}

@Composable
private fun Notice(text: String) {
    Surface(Modifier.fillMaxWidth().padding(top = 18.dp), RoundedCornerShape(18.dp), BrandGreenMist, border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenSoft)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Filled.Shield, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(12.dp))
            Text(text, color = TextSecondary, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun BottomActions(step: Int, loading: Boolean, onBack: () -> Unit, onContinue: () -> Unit) {
    Surface(color = Color.White, shadowElevation = 4.dp) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(0.8f).height(56.dp), shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }
            Button(
                onClick = onContinue,
                enabled = !loading,
                modifier = Modifier.weight(1.8f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) {
                Text(if (loading) "Creating..." else if (step == 3) "Continue to Dashboard" else if (step == 2) "Continue to Documents" else "Continue", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}
