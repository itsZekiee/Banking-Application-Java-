package com.bankingapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bankingapp.R
import com.bankingapp.ui.theme.BankingAppTheme

/** The Piggy Bank Central mascot image used throughout the app. */
@Composable
fun PiggyLogo(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp
) {
    Box(modifier = modifier.size(size)) {
        Image(
            painter = painterResource(R.drawable.piggy_central_bank_logo_transparent),
            contentDescription = "Piggy Bank Central logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(size)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PiggyLogoPreview() {
    BankingAppTheme {
        PiggyLogo(size = 140.dp)
    }
}
