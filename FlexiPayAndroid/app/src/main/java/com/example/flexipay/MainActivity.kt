package com.example.flexipay

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flexipay.ui.theme.FlexiPayTheme
import kotlinx.coroutines.delay

private const val PREFS_NAME = "FlexiPayPrefs"
private const val KEY_HAS_LAUNCHED_BEFORE = "hasLaunchedBefore"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val hasLaunchedBefore = prefs.getBoolean(KEY_HAS_LAUNCHED_BEFORE, false)

        setContent {
            FlexiPayTheme {
                var showingSplash by remember { mutableStateOf(!hasLaunchedBefore) }

                LaunchedEffect(key1 = true) {
                    if (showingSplash) {
                        delay(2000L) // 2-second delay
                        showingSplash = false
                        if (!hasLaunchedBefore) {
                            prefs.edit().putBoolean(KEY_HAS_LAUNCHED_BEFORE, true).apply()
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (showingSplash) {
                        SplashScreen()
                    } else {
                        MainAppScreen() // Main app with tabs
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for App Logo - Ensure you have R.drawable.app_logo or similar
        // You will need to add an AppLogo image to your drawables
        Text("[App Logo Here]")
        /* Example:
        Image(
            painter = painterResource(id = R.drawable.app_logo), // Replace with your logo resource
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
        */
    }
}

// Preview for the whole app entry point
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlexiPayTheme {
        var showingSplash by remember { mutableStateOf(true) }
        LaunchedEffect(key1 = Unit) {
            delay(2000L)
            showingSplash = false
        }
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (showingSplash) {
                SplashScreen()
            } else {
                MainAppScreen()
            }
        }
    }
} 