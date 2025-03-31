package com.example.flexipay

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flexipay.screens.ProfileScreen
import com.example.flexipay.screens.SettingsScreen
import com.example.flexipay.screens.SubscriptionsScreen // Keep name, represents Providers now
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.foundation.layout.RowScope

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Providers : Screen("providers", "Providers", Icons.Filled.List)
    object Profile : Screen("profile", "Profile", Icons.Filled.AccountCircle)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    Screen.Providers,
    Screen.Profile,
    Screen.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(navController: NavHostController = rememberNavController()) {
    val purchasedDurations = remember { mutableStateMapOf<String, String>() }

    val handlePurchase: (String, String) -> Unit = { serviceId, duration ->
        purchasedDurations[serviceId] = duration
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                BottomNavigationBarItems(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Providers.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Providers.route) { 
                SubscriptionsScreen(
                    purchasedDurations = purchasedDurations,
                    onPurchase = handlePurchase
                )
            }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
fun RowScope.BottomNavigationBarItems(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    bottomNavItems.forEach { screen ->
        NavigationBarItem(
            icon = { Icon(screen.icon, contentDescription = screen.label) },
            label = { Text(screen.label, style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == screen.route,
            onClick = {
                if (currentRoute != screen.route) {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
} 