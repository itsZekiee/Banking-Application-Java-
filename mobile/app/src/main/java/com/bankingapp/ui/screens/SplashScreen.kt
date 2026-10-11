package com.bankingapp.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bankingapp.ui.components.PiggyLogo
import com.bankingapp.ui.theme.BrandGreen
import com.bankingapp.ui.theme.BrandGreenBright
import com.bankingapp.ui.theme.BrandGreenMist
import com.bankingapp.ui.theme.BrandGreenSoft
import com.bankingapp.ui.theme.CoinGold
import com.bankingapp.ui.theme.SurfaceLight
import com.bankingapp.ui.theme.TextInk

@Composable
fun SplashScreen() {
    val transition = rememberInfiniteTransition(label = "splash motion")
    val drift by transition.animateFloat(
        initialValue = -24f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse),
        label = "splash drift"
    )
    val orbit by transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(3200), RepeatMode.Reverse),
        label = "splash orbit"
    )
    val logoScale by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "splash logo pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BrandGreenSoft, BrandGreenMist, SurfaceLight)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        FloatingOrb(
            color = BrandGreenBright.copy(alpha = 0.18f),
            size = 150.dp,
            modifier = Modifier.offset(x = (-92).dp, y = drift.dp)
        )
        FloatingOrb(
            color = CoinGold.copy(alpha = 0.24f),
            size = 56.dp,
            modifier = Modifier.offset(x = 112.dp, y = (drift * -0.65f).dp).rotate(orbit)
        )
        FloatingOrb(
            color = SurfaceLight.copy(alpha = 0.65f),
            size = 34.dp,
            modifier = Modifier.offset(x = 128.dp, y = (-128 + drift / 2).dp)
        )
        FloatingOrb(
            color = BrandGreen.copy(alpha = 0.12f),
            size = 72.dp,
            modifier = Modifier.offset(x = (-122).dp, y = (150 - drift / 2).dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(178.dp)
                    .background(SurfaceLight.copy(alpha = 0.72f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(154.dp)
                        .background(BrandGreenMist, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    PiggyLogo(
                        size = 126.dp,
                        modifier = Modifier
                            .rotate(orbit)
                            .graphicsLayer {
                                scaleX = logoScale
                                scaleY = logoScale
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Piggy Bank",
                color = TextInk,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "C E N T R A L",
                color = BrandGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }
    }
}

@Composable
private fun FloatingOrb(
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color, CircleShape)
    )
}
