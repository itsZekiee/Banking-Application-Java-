package com.bankingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bankingapp.data.model.AccountResponse
import com.bankingapp.R
import com.bankingapp.data.model.TransactionResponse
import com.bankingapp.ui.components.ErrorCard
import com.bankingapp.ui.components.LoadingIndicator
import com.bankingapp.ui.components.PiggyLogo
import com.bankingapp.ui.theme.BrandGreen
import com.bankingapp.ui.theme.BrandGreenDark
import com.bankingapp.ui.theme.BrandGreenBright
import com.bankingapp.ui.theme.BrandGreenMist
import com.bankingapp.ui.theme.BrandGreenSoft
import com.bankingapp.ui.theme.BrandGreenWash
import com.bankingapp.ui.theme.BorderGray
import com.bankingapp.ui.theme.CoinGold
import com.bankingapp.ui.theme.TextInk
import com.bankingapp.ui.theme.TextMuted
import com.bankingapp.ui.theme.TextSecondary
import com.bankingapp.viewmodel.AccountListUiState
import com.bankingapp.viewmodel.AccountViewModel
import com.bankingapp.viewmodel.TransactionHistoryUiState
import com.bankingapp.viewmodel.TransactionViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AccountListScreen(
    userId: Long,
    viewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    onSelectAccount: (String) -> Unit,
    onNavigateToCreateAccount: () -> Unit,
    onNavigateToDeposit: (String) -> Unit,
    onNavigateToWithdraw: (String) -> Unit,
    onNavigateToTransfer: (String) -> Unit,
    onNavigateToHistory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val accountState by viewModel.accountListState.collectAsState()
    val historyState by transactionViewModel.historyState.collectAsState()
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.fetchAccountsByUserId(userId)
        contentVisible = true
    }

    when (val state = accountState) {
        is AccountListUiState.Loading -> LoadingIndicator(modifier = modifier.fillMaxSize())
        is AccountListUiState.Error -> ErrorCard(message = state.message, modifier = modifier.padding(20.dp))
        is AccountListUiState.Success -> {
            val account = state.accounts.firstOrNull()
            if (account == null) {
                EmptyDashboard(onNavigateToCreateAccount, modifier)
            } else {
                LaunchedEffect(account.accountNumber) {
                    transactionViewModel.fetchTransactionHistory(account.accountNumber)
                }
                DashboardContent(
                    account = account,
                    transactions = (historyState as? TransactionHistoryUiState.Success)?.transactions.orEmpty(),
                    onBalanceClick = { onSelectAccount(account.accountNumber) },
                    onTransfer = { onNavigateToTransfer(account.accountNumber) },
                    onSend = { onNavigateToTransfer(account.accountNumber) },
                    onTopUp = { onNavigateToDeposit(account.accountNumber) },
                    onHistory = { onNavigateToHistory(account.accountNumber) },
                    onCreateAccount = onNavigateToCreateAccount,
                    modifier = modifier,
                    visible = contentVisible
                )
            }
        }
        else -> Box(modifier.fillMaxSize())
    }
}

@Composable
private fun DashboardContent(
    account: AccountResponse,
    transactions: List<TransactionResponse>,
    onBalanceClick: () -> Unit,
    onTransfer: () -> Unit,
    onSend: () -> Unit,
    onTopUp: () -> Unit,
    onHistory: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier,
    visible: Boolean
) {
    val displayName = account.userFullName?.substringBefore(" ")?.uppercase(Locale.getDefault()) ?: "THERE"
    Column(
        modifier = modifier.fillMaxSize().background(BrandGreenWash).verticalScroll(rememberScrollState())
    ) {
        DashboardHeader(displayName)
        Text(
            text = "● Active ${account.accountType.name.lowercase().replaceFirstChar { it.uppercase() }}",
            color = BrandGreenDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )
        BalanceCard(account = account, onClick = onBalanceClick)
        QuickActions(onTransfer, onSend, onTopUp, onHistory)
        GoalsSection()
        AnalyticsCard()
        TransactionsSection(transactions, onHistory)
        BottomNavigation(onCreateAccount)
    }
}

@Composable
private fun DashboardHeader(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(48.dp).background(BrandGreenMist, CircleShape), contentAlignment = Alignment.Center) {
            PiggyLogo(size = 40.dp)
        }
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Text("PIGGY BANK CENTRAL", color = BrandGreenDark, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.2.sp)
            Text("Welcome, $name", color = TextInk, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
        HeaderIcon(Icons.Filled.NotificationsNone)
        Spacer(Modifier.width(8.dp))
        HeaderIcon(Icons.Filled.Settings)
    }
}

@Composable
private fun HeaderIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        Modifier.size(38.dp).background(Color.White, CircleShape).clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(21.dp))
    }
}

@Composable
private fun BalanceCard(account: AccountResponse, onClick: () -> Unit) {
    val amount = NumberFormat.getNumberInstance(Locale.US).format(account.balance)
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        color = Color.Transparent
    ) {
        Box(
            Modifier.background(Color(0xFF061426), RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.card_template),
                contentDescription = "Piggy Bank Central card",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Column(Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PiggyLogo(size = 48.dp)
                    Text("Piggy Bank\nCentral", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.padding(start = 10.dp))
                }
                Spacer(Modifier.height(34.dp))
                Text("AVAILABLE BALANCE", color = BrandGreenBright, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(if (account.currency == "PHP") "₱" else account.currency, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(amount, color = Color.White, fontSize = 33.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 5.dp))
                    Text(".00", color = BrandGreenBright, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                Text("VIRTUAL • ${account.currency}", color = Color.White, fontSize = 11.sp, modifier = Modifier.align(Alignment.End).background(Color.White.copy(alpha = .18f), RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 7.dp))
            }
        }
    }
}

@Composable
private fun QuickActions(onTransfer: () -> Unit, onSend: () -> Unit, onTopUp: () -> Unit, onHistory: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        QuickAction(Icons.Filled.SwapHoriz, "Bank Transfer", onTransfer)
        QuickAction(Icons.Filled.Send, "Send", onSend)
        QuickAction(Icons.Filled.Add, "Top Up", onTopUp)
        QuickAction(Icons.Filled.ReceiptLong, "History", onHistory)
    }
}

@Composable
private fun QuickAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Box(Modifier.size(54.dp).background(BrandGreenDark, RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(25.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, color = TextInk, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun GoalsSection() {
    SectionTitle("Your goals", "3 active")
    GoalCard("✈", "Summer vacation", "Target by Aug 2024", "₱15,000", "₱50,000", "20 days to go", "40%", BrandGreen, 0.4f)
    GoalCard("⌂", "Real estate", "Condo down payment", "₱60,500", "₱230,000", "2 years to go", "62%", Color(0xFFFF7115), 0.62f)
    GoalCard("◇", "Magister education", "Data Science Master's", "₱12,000", "₱30,000", "8 months to go", "40%", Color(0xFF2E63DB), 0.4f)
}

@Composable
private fun SectionTitle(title: String, badge: String? = null) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextInk)
        if (badge != null) Text(badge, color = BrandGreenDark, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp).background(BrandGreenSoft, RoundedCornerShape(14.dp)).padding(horizontal = 9.dp, vertical = 5.dp))
        Spacer(Modifier.weight(1f))
        if (badge != null) Text("View all ›", color = BrandGreenDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GoalCard(icon: String, title: String, subtitle: String, current: String, target: String, remaining: String, percent: String, color: Color, progress: Float) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 7.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(Color.White)) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(42.dp).background(color.copy(alpha = .1f), RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
                    Text(icon, color = color, fontSize = 22.sp)
                }
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(title, fontWeight = FontWeight.Bold, color = TextInk, fontSize = 16.sp)
                    Text(subtitle, color = TextMuted, fontSize = 13.sp)
                }
                Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(17.dp))
            }
            Spacer(Modifier.height(13.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(current, fontWeight = FontWeight.Bold, color = TextInk, fontSize = 14.sp)
                Text(target, color = TextMuted, fontSize = 14.sp)
            }
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxWidth().height(9.dp).background(BorderGray, RoundedCornerShape(8.dp))) {
                Box(Modifier.fillMaxWidth(progress).height(9.dp).background(color, RoundedCornerShape(8.dp)))
            }
            Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(remaining, color = TextMuted, fontSize = 12.sp)
                Text(percent, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun AnalyticsCard() {
    Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(Color.White)) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Saving Analytics", color = TextInk, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text("Average per month  ₱1,202", color = TextMuted, fontSize = 13.sp)
                }
                Text("↗ +112.00%", color = BrandGreenDark, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.background(BrandGreenMist, RoundedCornerShape(18.dp)).padding(horizontal = 10.dp, vertical = 8.dp))
            }
            Row(Modifier.fillMaxWidth().padding(top = 22.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                listOf("Jan" to .55f, "Feb" to .42f, "Mar" to .32f, "Apr" to .45f, "May" to .8f, "Jun" to .1f).forEach { (month, value) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.width(25.dp).height((112 * value).dp).background(if (month == "May") BrandGreenBright else Color(0xFF26313B), RoundedCornerShape(14.dp)))
                        Spacer(Modifier.height(7.dp))
                        Text(month, color = if (month == "May") BrandGreenDark else TextMuted, fontSize = 11.sp, fontWeight = if (month == "May") FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionsSection(transactions: List<TransactionResponse>, onHistory: () -> Unit) {
    SectionTitle("Recent Transactions")
    if (transactions.isEmpty()) {
        Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(Color.White)) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ReceiptLong, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(24.dp))
                Text("No transactions yet", color = TextSecondary, fontSize = 14.sp, modifier = Modifier.padding(start = 12.dp))
            }
        }
    } else {
        transactions.take(3).forEach { transaction ->
            TransactionCard(transaction)
        }
    }
    Text("See all", color = BrandGreenDark, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End).padding(horizontal = 24.dp, vertical = 8.dp).clickable(onClick = onHistory))
}

@Composable
private fun TransactionCard(transaction: TransactionResponse) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(BrandGreenMist, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.ArrowUpward, contentDescription = null, tint = BrandGreenDark)
            }
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(transaction.description ?: transaction.transactionType.name, fontWeight = FontWeight.Bold, color = TextInk)
                Text(transaction.timestamp ?: "", color = TextMuted, fontSize = 12.sp)
            }
            Text("₱${transaction.amount}", color = BrandGreenDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun BottomNavigation(onCreateAccount: () -> Unit) {
    Surface(color = BrandGreenDark, shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp), modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(25.dp))
            Icon(Icons.Filled.ReceiptLong, contentDescription = "Transactions", tint = BrandGreenSoft, modifier = Modifier.size(25.dp))
            Box(Modifier.size(58.dp).background(Color.White, CircleShape).clickable(onClick = onCreateAccount), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Add, contentDescription = "Add account", tint = BrandGreenDark, modifier = Modifier.size(30.dp))
            }
            Icon(Icons.Filled.Savings, contentDescription = "Savings", tint = BrandGreenSoft, modifier = Modifier.size(25.dp))
            Icon(Icons.Filled.CreditCard, contentDescription = "Cards", tint = BrandGreenSoft, modifier = Modifier.size(25.dp))
        }
    }
}

@Composable
private fun EmptyDashboard(onCreateAccount: () -> Unit, modifier: Modifier) {
    Column(modifier.fillMaxSize().background(BrandGreenWash).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Filled.Warning, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(48.dp))
        Text("No accounts found", color = TextInk, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 12.dp))
        Text("Open your first account to see your dashboard.", color = TextSecondary, modifier = Modifier.padding(top = 6.dp))
        Text("Open account", color = BrandGreenDark, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 20.dp).clickable(onClick = onCreateAccount))
    }
}
