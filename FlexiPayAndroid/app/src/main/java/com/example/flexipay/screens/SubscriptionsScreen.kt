package com.example.flexipay.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Import all filled icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flexipay.R // Import R class
import com.example.flexipay.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.animation.SizeTransform
import androidx.compose.material3.surfaceColorAtElevation
import com.example.flexipay.network.PurchaseNotifier


data class SubscriptionService(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @DrawableRes val iconResId: Int,
    val themeColor: Color = AppBlue
)

val sampleSubscriptions = listOf(
    SubscriptionService(name = "JioHotstar", iconResId = R.drawable.jiohotstar),
    SubscriptionService(name = "SonyLIV", iconResId = R.drawable.sonyliv),
    SubscriptionService(name = "Amazon Prime Video", iconResId = R.drawable.amazonprimevideo)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    purchasedDurations: Map<String, String>, 
    onPurchase: (serviceId: String, duration: String) -> Unit
) {
    var selectedService by remember { mutableStateOf<SubscriptionService?>(null) }
    var isSheetContentPurchased by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Initialize WebSocket connection
    LaunchedEffect(Unit) {
        // Replace with your ngrok HTTP URL
        PurchaseNotifier.initialize("https://2769-2607-f470-6-4001-94fe-acb2-d40-4f46.ngrok-free.app")
    }

    // Cleanup when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            PurchaseNotifier.cleanup()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Providers",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(sampleSubscriptions) { service ->
                val currentPurchase = purchasedDurations[service.id]
                SubscriptionOptionCard(
                    providerName = service.name,
                    iconRes = service.iconResId,
                    isPurchased = currentPurchase != null,
                    purchasedDuration = currentPurchase,
                    onClick = {
                        selectedService = service
                        isSheetContentPurchased = (currentPurchase != null)
                        scope.launch { modalSheetState.show() }
                    }
                )
            }
        }
    }

    if (modalSheetState.isVisible && selectedService != null) {
        val serviceToUpdate = selectedService!!
        ModalBottomSheet(
            onDismissRequest = {
                 scope.launch {
                    modalSheetState.hide()
                    selectedService = null
                 }
            },
            sheetState = modalSheetState,
        ) {
            SubscriptionOptionsSheetContent(
                serviceName = serviceToUpdate.name,
                isPurchased = isSheetContentPurchased,
                onPurchaseClick = { duration ->
                    isSheetContentPurchased = true
                    onPurchase(serviceToUpdate.id, duration)
                    // Notify the Python server
                    PurchaseNotifier.notifyPurchase(serviceToUpdate.name, duration)
                    println("Purchase requested for ${serviceToUpdate.name} - $duration")
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubscriptionOptionCard(
    providerName: String,
    iconRes: Int,
    isPurchased: Boolean,
    purchasedDuration: String?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(alpha = 0.85f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "$providerName logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = providerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    lineHeight = MaterialTheme.typography.titleMedium.lineHeight * 0.9f
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            AnimatedContent(
                targetState = isPurchased,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) with fadeOut(animationSpec = tween(200))
                }
            ) { purchasedState ->
                if (purchasedState) {
                    Box(
                        modifier = Modifier
                            .widthIn(min = 100.dp)
                            .height(36.dp)
                            .clip(RoundedCornerShape(50))
                            .background(AppGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = purchasedDuration?.let { "$it Left" } ?: "Active",
                            color = AppGreen,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .widthIn(min = 100.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("Buy Now", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Card Not Purchased")
@Composable
fun SubscriptionOptionCardPreview_NotPurchased() {
    FlexiPayTheme {
        SubscriptionOptionCard(providerName = "JioHotstar", iconRes = R.drawable.jiohotstar, isPurchased = false, purchasedDuration = null, onClick = {})
    }
}

@Preview(showBackground = true, name = "Card Purchased")
@Composable
fun SubscriptionOptionCardPreview_Purchased() {
    FlexiPayTheme {
        SubscriptionOptionCard(providerName = "JioHotstar", iconRes = R.drawable.jiohotstar, isPurchased = true, purchasedDuration = "1 Week", onClick = {})
    }
}

/* Example of how a preview *could* be adapted, though often simpler to preview SubscriptionOptionCard directly:
@Preview(showBackground = true)
@Composable
fun SubscriptionsScreenPreview() {
    FlexiPayTheme {
        SubscriptionsScreen(purchasedDurations = mapOf(), onPurchase = { _, _ -> })
    }
}
*/ 