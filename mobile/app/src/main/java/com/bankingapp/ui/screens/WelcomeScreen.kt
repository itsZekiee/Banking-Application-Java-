package com.bankingapp.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bankingapp.ui.components.PiggyLogo
import com.bankingapp.ui.theme.BankingAppTheme
import com.bankingapp.ui.theme.BrandGreen
import com.bankingapp.ui.theme.BrandGreenBright
import com.bankingapp.ui.theme.BrandGreenMist
import com.bankingapp.ui.theme.BrandGreenSoft
import com.bankingapp.ui.theme.BrandGreenWash
import com.bankingapp.ui.theme.BorderGray
import com.bankingapp.ui.theme.CoinGold
import com.bankingapp.ui.theme.PiggyInk
import com.bankingapp.ui.theme.SurfaceLight
import com.bankingapp.ui.theme.TextInk
import com.bankingapp.ui.theme.TextMuted
import com.bankingapp.ui.theme.TextSecondary

@Composable
fun WelcomeScreen(
    onOpenAccount: () -> Unit,
    onLogIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "welcome motion")
    val glowOffset by transition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(4200), RepeatMode.Reverse),
        label = "welcome glow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BrandGreenSoft, BrandGreenMist, BrandGreenWash)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .graphicsLayer { translationX = glowOffset; translationY = glowOffset / 2f }
                .background(BrandGreenBright.copy(alpha = 0.08f), CircleShape)
                .align(Alignment.TopEnd)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandWordmark(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 16.dp, end = 24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            HeroBadge(transition = transition)

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextInk)) { append("Smarter savings,\n") }
                    withStyle(SpanStyle(color = BrandGreen)) { append("joyful banking.") }
                },
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Automate your high-yield stash, eliminate hidden fees, and watch your wealth compound daily.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 28.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HighlightCard(
                    icon = Icons.Filled.TrendingUp,
                    value = "5.10% APY",
                    label = "Daily Payout",
                    modifier = Modifier.weight(1f)
                )
                HighlightCard(
                    icon = Icons.Filled.VerifiedUser,
                    value = "$250k FDIC",
                    label = "Full Coverage",
                    modifier = Modifier.weight(1f)
                )
                HighlightCard(
                    icon = Icons.Filled.AttachMoney,
                    value = "$0 Fees",
                    label = "No Minimums",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            ActionPanel(
                onOpenAccount = onOpenAccount,
                onLogIn = onLogIn
            )
        }
    }
}

@Composable
private fun BrandWordmark(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(SurfaceLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            PiggyLogo(size = 34.dp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "Piggy Bank",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextInk
            )
            Text(
                text = "C E N T R A L",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = BrandGreen
            )
        }
    }
}

@Composable
private fun HeroBadge(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "hero motion")
    HeroBadge(modifier, transition)
}

@Composable
private fun HeroBadge(
    modifier: Modifier = Modifier,
    transition: androidx.compose.animation.core.InfiniteTransition
) {
    val heroScale by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "hero pulse"
    )
    val heroOffset by transition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(2400), RepeatMode.Reverse),
        label = "hero float"
    )

    Box(
        modifier = modifier.graphicsLayer {
            translationY = heroOffset
            scaleX = heroScale
            scaleY = heroScale
        },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(196.dp)
                .background(SurfaceLight.copy(alpha = 0.55f), CircleShape)
                .padding(10.dp)
                .border(2.dp, BrandGreenBright.copy(alpha = 0.45f), CircleShape)
                .background(SurfaceLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            PiggyLogo(size = 138.dp)
        }

        Row(
            modifier = Modifier
                .background(PiggyInk, RoundedCornerShape(50))
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = CoinGold,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "5.10% APY Vault",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SurfaceLight
            )
        }
    }
}

@Composable
private fun HighlightCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(SurfaceLight.copy(alpha = 0.85f), RoundedCornerShape(18.dp))
            .border(1.dp, BorderGray.copy(alpha = 0.8f), RoundedCornerShape(18.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(BrandGreenMist, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = TextInk,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionPanel(
    onOpenAccount: () -> Unit,
    onLogIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SurfaceLight,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onOpenAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGreen,
                    contentColor = SurfaceLight
                )
            ) {
                Text(
                    text = "Open Free Account",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onLogIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGreenMist,
                    contentColor = BrandGreen
                )
            ) {
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            SecurityFootnote(text = "Member FDIC • Bank-grade 256-bit AES encryption")
        }
    }
}

@Composable
internal fun SecurityFootnote(
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = BrandGreen
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun WelcomeScreenPreview() {
    BankingAppTheme {
        WelcomeScreen(onOpenAccount = {}, onLogIn = {})
    }
}
