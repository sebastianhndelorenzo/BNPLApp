package com.example.flexipay.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flexipay.R // Assuming you have a drawable named ic_launcher_foreground or similar
import com.example.flexipay.ui.theme.FlexiPayTheme
import kotlin.math.min
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// --- Country Data Structure REMOVED ---
// --- getSortedCountries function REMOVED ---

// --- Phone Number Visual Transformation (Simplified for India only) ---
class PhoneNumberVisualTransformation(private val isIndia: Boolean) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        if (text.text.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = if (isIndia) {
            formatIndianNumber(text.text)
        } else {
            formatUSNumber(text.text)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var nonDigits = 0
                val originalString = text.text
                var i = 0
                var j = 0
                while (i < formattedText.length && j < originalString.length && j < offset) {
                    if (!formattedText[i].isDigit()) {
                        nonDigits++
                    } else {
                        j++
                    }
                    i++
                }
                while (i < formattedText.length && j == offset && !formattedText[i].isDigit()) {
                    nonDigits++
                    i++
                }
                return min(offset + nonDigits, formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var digits = 0
                for (i in 0 until min(offset, formattedText.length)) {
                    if (formattedText[i].isDigit()) {
                        digits++
                    }
                }
                return min(digits, text.text.length)
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

private fun formatIndianNumber(text: String): String {
    val trimmed = text.take(10)
    return buildString {
        if (trimmed.isNotEmpty()) {
            append(trimmed.substring(0, min(3, trimmed.length)))
        }
        if (trimmed.length > 3) {
            append(" ")
            append(trimmed.substring(3, min(6, trimmed.length)))
        }
        if (trimmed.length > 6) {
            append(" ")
            append(trimmed.substring(6))
        }
    }
}

private fun formatUSNumber(text: String): String {
    val trimmed = text.take(10)
    return buildString {
        if (trimmed.isNotEmpty()) {
            append("(")
            append(trimmed.substring(0, min(3, trimmed.length)))
            if (trimmed.length > 3) {
                append(") ")
                append(trimmed.substring(3, min(6, trimmed.length)))
                if (trimmed.length > 6) {
                    append("-")
                    append(trimmed.substring(6))
                }
            }
        }
    }
}

// Other formatters removed (formatNorthAmericanNumber, formatUKNumber)

@Serializable // Make data class serializable for JSON
data class VerificationRequest(val phoneNumber: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var isIndia by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Remember Ktor client
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    val isPhoneNumberValid = remember(phoneNumber) {
        phoneNumber.isNotBlank() && phoneNumber.length == 10
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 30.dp)
            )

            Text(
                text = "Welcome to FlexiPay",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Please enter your phone number to get started.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable { isIndia = !isIndia }
                        .width(85.dp)
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isIndia) "🇮🇳" else "🇺🇸",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (isIndia) "+91" else "+1",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                BasicTextField(
                    value = phoneNumber,
                    onValueChange = { newValue ->
                        phoneNumber = newValue.filter { it.isDigit() }.take(10)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    visualTransformation = PhoneNumberVisualTransformation(isIndia),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (phoneNumber.isEmpty()) {
                                Text("Phone Number", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Text(
                text = "FlexiPay currently only operates in India.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                val countryCode = if (isIndia) "+91" else "+1"
                val fullPhoneNumber = "$countryCode$phoneNumber"
                println("Sending phone number to server: $fullPhoneNumber")

                scope.launch {
                    try {
                        // Use the ngrok URL provided by the user
                        val response: HttpResponse = client.post("https://b1a9-2607-f470-6-4001-94fe-acb2-d40-4f46.ngrok-free.app/send-verification") {
                            contentType(ContentType.Application.Json)
                            setBody(VerificationRequest(phoneNumber = fullPhoneNumber))
                        }

                        if (response.status == HttpStatusCode.OK) {
                            println("Server received number successfully.")
                            Toast.makeText(context, "Verification initiated.", Toast.LENGTH_SHORT).show()
                            onOnboardingComplete()
                        } else {
                            println("Server error: ${response.status}")
                            Toast.makeText(context, "Server error: ${response.status}", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        // Handle network errors (e.g., server not reachable, incorrect IP)
                        println("Network request failed: ${e.message}")
                        Toast.makeText(context, "Failed to connect to server: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = isPhoneNumberValid,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, name = "Onboarding Screen")
@Composable
fun OnboardingScreenPreview() {
    FlexiPayTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            OnboardingScreen(onOnboardingComplete = {})
        }
    }
} 