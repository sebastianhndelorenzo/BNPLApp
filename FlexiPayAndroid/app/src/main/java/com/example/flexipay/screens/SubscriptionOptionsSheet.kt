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
    isPurchased: Boolean,
    onPurchaseClick: (String) -> Unit
) {
    var selectedDurationIndex by remember { mutableStateOf(0) }
    var selectedPaymentOptionIndex by remember { mutableStateOf(0) }

    val durations = listOf(
        Triple("One Month", "₹300", "One Month"),
        Triple("One Week", "₹100", "One Week"),
        Triple("One Day", "₹20", "One Day")
    )

    val monthlyPaymentOptions = listOf(
        Pair("Buy Now, Pay Later", "₹0 upfront, then 4 payments of ₹75 weekly"),
        Pair("Even Spread", "₹75 upfront, then 4 payments of ₹75 weekly (+ ₹20 cashback if paid in full)")
    )

    val weeklyPaymentOptions = listOf(
        Pair("Buy Now, Pay Later", "₹0 upfront, then 4 payments of ₹25 weekly"),
        Pair("Even Spread", "₹25 upfront, then 4 payments of ₹18 weekly (+ ₹7 cashback if paid in full)")
    )

    val dailyPaymentOptions = listOf(
        Pair("Buy Now, Pay Later", "₹0 upfront, then 4 payments of ₹5 weekly"),
        Pair("Even Spread", "₹5 upfront, then 4 payments of ₹4 weekly (+ ₹2 cashback if paid in full)")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 32.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = if (isPurchased) "Email for Login" else "Choose duration for $serviceName",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            AnimatedContent(
                targetState = isPurchased,
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "SheetContentAnimation"
            ) { showEmailView ->
                if (showEmailView) {
                    EmailDisplayView(emailToCopy = "BNPLTestEmail1@gmail.com")
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            durations.forEachIndexed { index, (label, price, _) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (selectedDurationIndex == index)
                                                MaterialTheme.colorScheme.primaryContainer
                                            else
                                                MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                                        )
                                        .clickable { 
                                            selectedDurationIndex = index
                                            selectedPaymentOptionIndex = 0 
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (selectedDurationIndex == index)
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = price,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (selectedDurationIndex == index)
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(modifier = Modifier.padding(top = 24.dp)) {
                                Text(
                                    text = "Payment Plan:",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                val currentPaymentOptions = when (selectedDurationIndex) {
                                    0 -> monthlyPaymentOptions
                                    1 -> weeklyPaymentOptions
                                    else -> dailyPaymentOptions
                                }
                                
                                currentPaymentOptions.forEachIndexed { index, (title, subtitle) ->
                                    PaymentOptionItem(
                                        title = title,
                                        subtitle = subtitle,
                                        isSelected = selectedPaymentOptionIndex == index,
                                        onClick = { selectedPaymentOptionIndex = index }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!isPurchased) {
             Spacer(modifier = Modifier.weight(1f))
             Button(
                onClick = {
                    onPurchaseClick(durations[selectedDurationIndex].third)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Purchase ${durations[selectedDurationIndex].first} for ${durations[selectedDurationIndex].second}",
                    style = MaterialTheme.typography.bodyLarge
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(8.dp))
        Text(price, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
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
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(MaterialTheme.colorScheme.primary))
        ) {
            Text(text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(price, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            "Most Popular",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}

@Composable
fun PaymentOptionItem(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(alpha = 0.85f))
            .background(AppBlue.copy(alpha = 0.03f))
            .clickable(enabled = !showCopiedFeedback) { 
                 val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                 val clip = ClipData.newPlainText("Email Address", emailToCopy)
                 clipboard.setPrimaryClip(clip)
                 showCopiedFeedback = true
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Use this email to sign in:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = emailToCopy,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = showCopiedFeedback,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) with fadeOut(animationSpec = tween(200))
                },
                label = "CopyFeedbackAnimation"
            ) { isCopied ->
                if (isCopied) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Check, contentDescription = "Copied", tint = AppGreen, modifier = Modifier.size(18.dp))
                        Text("Copied", color = AppGreen, style = MaterialTheme.typography.labelMedium)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy Email", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                        Text("Copy", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelMedium)
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
        SubscriptionOptionsSheetContent(serviceName = "Sample Service", isPurchased = false, onPurchaseClick = {})
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