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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flexipay.screens.OnboardingScreen
import com.example.flexipay.ui.theme.FlexiPayTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    companion object {
        // --- Development Flags ---
        // Set to true to always show the onboarding screen, regardless of SharedPreferences
        const val DEBUG_FORCE_ONBOARDING = true
        // Set to true to always skip the onboarding screen (takes precedence over FORCE)
        const val DEBUG_SKIP_ONBOARDING = false
    }

    // SharedPreferences key
    private val prefsName = "FlexiPayPrefs"
    private val keyOnboardingComplete = "onboarding_complete"

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        installSplashScreen()
        
        super.onCreate(savedInstanceState)

        setContent {
            FlexiPayTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Read initial state from SharedPreferences
                    val context = LocalContext.current
                    val sharedPreferences = remember { context.getSharedPreferences(prefsName, Context.MODE_PRIVATE) }
                    val hasCompletedOnboardingNormally = remember { sharedPreferences.getBoolean(keyOnboardingComplete, false) }

                    // Determine initial state based on debug flags and saved state
                    val initialState = when {
                        DEBUG_SKIP_ONBOARDING -> false // Skip onboarding
                        DEBUG_FORCE_ONBOARDING -> true // Force onboarding
                        else -> !hasCompletedOnboardingNormally // Normal logic
                    }

                    // State to control which screen is shown
                    var showOnboarding by rememberSaveable { mutableStateOf(initialState) }

                    // Callback function to complete onboarding
                    val completeOnboarding = {
                        // Only save if not in a debug override mode
                        if (!DEBUG_FORCE_ONBOARDING && !DEBUG_SKIP_ONBOARDING) {
                            sharedPreferences.edit().putBoolean(keyOnboardingComplete, true).apply()
                        }
                        showOnboarding = false
                        println("Onboarding Completed (Debug flags: FORCE=$DEBUG_FORCE_ONBOARDING, SKIP=$DEBUG_SKIP_ONBOARDING)")
                    }

                    // Conditionally display Onboarding or Main App
                    if (showOnboarding) {
                        OnboardingScreen(onOnboardingComplete = completeOnboarding)
                    } else {
                        MainAppScreen()
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
        Text("[App Logo Here]")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlexiPayTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            MainAppScreen()
        }
    }
}

@Preview(showBackground = true, name = "Onboarding Preview")
@Composable
fun OnboardingPreview() {
    FlexiPayTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            OnboardingScreen(onOnboardingComplete = { /* Preview doesn't complete */ })
        }
    }
} 