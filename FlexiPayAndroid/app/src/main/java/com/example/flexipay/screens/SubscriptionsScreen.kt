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
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(AppGradientStart.copy(alpha = 0.8f), AppGradientEnd.copy(alpha = 0.6f))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Text(
                    text = "Providers",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(sampleSubscriptions) { service ->
                val currentPurchase = purchasedDurations[service.id]
                SubscriptionOptionCard(
                    service = service,
                    purchasedDuration = currentPurchase,
                    onClick = {
                        selectedService = service
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
                onOptionSelected = { duration ->
                    onPurchase(serviceToUpdate.id, duration)
                    println("Purchase requested for ${serviceToUpdate.name} - $duration")
                    // Optionally hide sheet after selection
                    // scope.launch { modalSheetState.hide(); selectedService = null }
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubscriptionOptionCard(
    service: SubscriptionService,
    purchasedDuration: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f) // Slightly more opaque
        ),
        border = BorderStroke(1.dp, service.themeColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = service.iconResId),
                contentDescription = service.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            AnimatedContent(
                targetState = purchasedDuration != null,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(500)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(500)))
                    .togetherWith(fadeOut(animationSpec = tween(300)))
                    .using(SizeTransform(clip = false))
                },
                label = "PurchaseIndicatorAnimation"
            ) { isPurchased ->
                if (isPurchased) {
                    val remainingText = when (purchasedDuration) {
                        "1 Day" -> "1 Day Remaining"
                        "1 Week" -> "7 Days Remaining"
                        "1 Month" -> "30 Days Remaining"
                        else -> "Purchased"
                    }
                    Text(
                        text = remainingText,
                        color = AppGreen, // Use AppGreen from theme
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(AppGreen.copy(alpha = 0.1f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                } else {
                    Text(
                        text = "Buy Now",
                        color = service.themeColor,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(service.themeColor.copy(alpha = 0.1f), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Card Not Purchased")
@Composable
fun SubscriptionOptionCardPreview_NotPurchased() {
    FlexiPayTheme {
        SubscriptionOptionCard(service = sampleSubscriptions[0], purchasedDuration = null, onClick = {})
    }
}

@Preview(showBackground = true, name = "Card Purchased")
@Composable
fun SubscriptionOptionCardPreview_Purchased() {
    FlexiPayTheme {
        SubscriptionOptionCard(service = sampleSubscriptions[1], purchasedDuration = "1 Week", onClick = {})
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