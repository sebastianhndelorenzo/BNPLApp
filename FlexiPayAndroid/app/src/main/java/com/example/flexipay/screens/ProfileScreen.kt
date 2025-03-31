package com.example.flexipay.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.flexipay.ui.theme.AppGradientEnd
import com.example.flexipay.ui.theme.AppGradientStart
import com.example.flexipay.ui.theme.FlexiPayTheme

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                 Brush.linearGradient(
                    colors = listOf(AppGradientStart.copy(alpha = 0.8f), AppGradientEnd.copy(alpha = 0.6f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Profile Page",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FlexiPayTheme {
        ProfileScreen()
    }
} 