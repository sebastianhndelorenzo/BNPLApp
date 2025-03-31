package com.example.flexipay.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flexipay.ui.theme.AppBlue
import com.example.flexipay.ui.theme.AppGreen
import com.example.flexipay.ui.theme.FlexiPayTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubscriptionOptionsSheetContent(
    serviceName: String,
    onOptionSelected: (String) -> Unit
) {
    var optionSelected by remember { mutableStateOf(false) }

    val priceDay = "$0.99"
    val priceWeek = "$2.99"
    val priceMonth = "$7.99"
    val emailToCopy = "BNPLTestEmail1@gmail.com"

    Column(
        modifier = Modifier
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = if (optionSelected) "Email for Login" else "Choose duration for $serviceName",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AnimatedContent(
            targetState = optionSelected,
            transitionSpec = {
                if (targetState) {
                    slideInHorizontally { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() with
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            }
        ) { isEmailViewVisible ->
            if (isEmailViewVisible) {
                EmailDisplayView(emailToCopy = emailToCopy)
            } else {
                OptionsSelectionView(
                    serviceName = serviceName,
                    priceDay = priceDay,
                    priceWeek = priceWeek,
                    priceMonth = priceMonth,
                    onSelect = { duration ->
                        println("Selected $duration for $serviceName")
                        optionSelected = true
                        onOptionSelected(duration)
                    }
                )
            }
        }
    }
}

@Composable
fun OptionsSelectionView(
    serviceName: String,
    priceDay: String,
    priceWeek: String,
    priceMonth: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OptionButton(text = "1 Day Access", price = priceDay) { onSelect("1 Day") }
        PopularOption(
            text = "1 Week Access",
            price = priceWeek,
            onClick = { onSelect("1 Week") }
        )
        OptionButton(text = "1 Month Access", price = priceMonth) { onSelect("1 Month") }
    }
}

@Composable
fun OptionButton(
    text: String,
    price: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, modifier = Modifier.weight(1f))
        Text(price, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun PopularOption(
    text: String,
    price: String,
    onClick: () -> Unit
) {
    Column {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(AppBlue))
        ) {
            Text(text, modifier = Modifier.weight(1f))
            Text(price, fontWeight = FontWeight.SemiBold)
        }
        Text(
            "Most Popular",
            color = AppBlue,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EmailDisplayView(emailToCopy: String) {
    var showCopiedFeedback by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(showCopiedFeedback) {
        if (showCopiedFeedback) {
            delay(2000L)
            showCopiedFeedback = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Use this email to sign in:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.pointerInput(Unit) { // Consume long press
                    detectTapGestures(onLongPress = { })
                }
            )
            Text(
                text = emailToCopy,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.pointerInput(Unit) { // Consume long press
                    detectTapGestures(onLongPress = { })
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable {
                    if (!showCopiedFeedback) {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Email Address", emailToCopy)
                        clipboard.setPrimaryClip(clip)
                        showCopiedFeedback = true
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = showCopiedFeedback,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) with fadeOut(animationSpec = tween(200))
                }
            ) {
                if (it) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Check, contentDescription = "Copied", tint = AppGreen)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copied", color = AppGreen, style = MaterialTheme.typography.labelLarge)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy Email", tint = AppBlue)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy", color = AppBlue, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubscriptionOptionsSheetContent_OptionsPreview() {
    FlexiPayTheme {
        SubscriptionOptionsSheetContent(serviceName = "Sample Service", onOptionSelected = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SubscriptionOptionsSheetContent_EmailPreview() {
    FlexiPayTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Email for Login",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            EmailDisplayView(emailToCopy = "test.email@example.com")
        }
    }
} 