package com.kabadimitra.collector.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kabadimitra.collector.core.designsystem.GreenSuccess
import com.kabadimitra.collector.core.designsystem.NavyCard
import com.kabadimitra.collector.core.designsystem.NavyDark
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.CustomNumericKeypad
import com.kabadimitra.collector.core.designsystem.components.KmCTAButton
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton

@Composable
fun OnboardingScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    onVoiceSpeak: (String) -> Unit = {}
) {
    var selectedLanguage by remember { mutableStateOf("hi") } // "hi", "mr", "en"
    var phoneNumber by remember { mutableStateOf("9821054321") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header Brand & Icon
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(OrangeLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Recycling,
                        contentDescription = "Logo",
                        tint = OrangePrimary,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "कबाड़ मित्र",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = NavyDark
                )
                Text(
                    text = "सच्चा तोल • सही दाम • पक्की पर्ची",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = OrangePrimary
                )

                // Voice Hint Banner
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = OrangeLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VoiceIconButton(
                            onClick = {
                                val textToSpeak = if (!isOtpSent) {
                                    "अपनी भाषा चुनें और मोबाइल नंबर दर्ज करें"
                                } else {
                                    "चार अंकों का ओटीपी दर्ज करें"
                                }
                                onVoiceSpeak(textToSpeak)
                            },
                            size = 38.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (!isOtpSent) "अपनी भाषा चुनें और फोन नंबर डालें" else "ओटीपी कोड 4812 दर्ज करें",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = NavyDark
                        )
                    }
                }

                // Language Selector Row
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "भाषा चुनें (Select Language)",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF64748B),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        "hi" to "हिंदी",
                        "mr" to "मराठी",
                        "en" to "English"
                    ).forEach { (code, label) ->
                        val isSelected = selectedLanguage == code
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NavyDark else SurfaceLight)
                                .border(
                                    1.dp,
                                    if (isSelected) NavyDark else Color(0xFFCBD5E1),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedLanguage = code },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = if (isSelected) SurfaceLight else NavyDark
                                )
                            )
                        }
                    }
                }

                // Phone / OTP Input Cards
                Spacer(modifier = Modifier.height(20.dp))
                if (!isOtpSent) {
                    Text(
                        text = "मोबाइल नंबर (Phone Number)",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF64748B),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceLight)
                            .border(1.5.dp, OrangePrimary, RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = OrangePrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "+91  ",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                            Text(
                                text = phoneNumber.ifEmpty { "दस अंकों का नंबर दर्ज करें" },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (phoneNumber.isEmpty()) Color(0xFF94A3B8) else NavyDark
                                )
                            )
                        }
                    }
                } else {
                    Text(
                        text = "ओटीपी कोड (Enter 4-Digit OTP)",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF64748B),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0 until 4) {
                            val char = otpCode.getOrNull(i)?.toString() ?: ""
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceLight)
                                    .border(
                                        2.dp,
                                        if (char.isNotEmpty()) GreenSuccess else Color(0xFFCBD5E1),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = NavyDark
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Keypad & Bottom CTA Action
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isOtpSent) {
                    KmCTAButton(
                        text = "ओटीपी भेजें (Send OTP)",
                        onClick = {
                            if (phoneNumber.length >= 10) {
                                isOtpSent = true
                                otpCode = "4812" // Pre-fill default demo OTP
                            }
                        },
                        trailingIcon = Icons.Default.ArrowForward,
                        enabled = phoneNumber.isNotEmpty()
                    )
                } else {
                    KmCTAButton(
                        text = "लॉगिन करें (Verify & Enter)",
                        onClick = onLoginSuccess,
                        trailingIcon = Icons.Default.Check,
                        enabled = otpCode.length >= 4
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                CustomNumericKeypad(
                    onDigitClick = { digit ->
                        if (!isOtpSent) {
                            if (phoneNumber.length < 10) phoneNumber += digit
                        } else {
                            if (otpCode.length < 4) otpCode += digit
                        }
                    },
                    onBackspaceClick = {
                        if (!isOtpSent) {
                            if (phoneNumber.isNotEmpty()) phoneNumber = phoneNumber.dropLast(1)
                        } else {
                            if (otpCode.isNotEmpty()) otpCode = otpCode.dropLast(1)
                        }
                    },
                    onClearClick = {
                        if (!isOtpSent) phoneNumber = "" else otpCode = ""
                    },
                    allowDecimal = false
                )
            }
        }
    }
}
