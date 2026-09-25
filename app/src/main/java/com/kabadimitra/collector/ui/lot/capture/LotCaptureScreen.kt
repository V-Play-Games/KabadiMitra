package com.kabadimitra.collector.ui.lot.capture

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.kabadimitra.collector.core.designsystem.GreenDark
import com.kabadimitra.collector.core.designsystem.GreenLight
import com.kabadimitra.collector.core.designsystem.GreenSuccess
import com.kabadimitra.collector.core.designsystem.NavyCard
import com.kabadimitra.collector.core.designsystem.NavyDark
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.ChipType
import com.kabadimitra.collector.core.designsystem.components.CustomNumericKeypad
import com.kabadimitra.collector.core.designsystem.components.KmCTAButton
import com.kabadimitra.collector.core.designsystem.components.StatusChip
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotCaptureScreen(
    onNavigateBack: () -> Unit,
    onProceedToEstimate: (lotId: String, weightKg: Double, material: String) -> Unit,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var hasCapturedPhoto by remember { mutableStateOf(true) }
    var detectedMaterial by remember { mutableStateOf("PET Plastic (बोतलें)") }
    var confidencePercent by remember { mutableStateOf(94) }
    var isMaterialConfirmed by remember { mutableStateOf(true) }
    var weightInput by remember { mutableStateOf("28.5") }
    var showMaterialPicker by remember { mutableStateOf(false) }

    val materialsList = listOf(
        "PET Plastic (बोतलें)",
        "Cardboard / गत्ता",
        "Iron / Loha (कबाड़ लोहा)",
        "Copper Wire (तांबा)",
        "Aluminium (एल्युमिनियम)",
        "HDPE Drums (कड़क प्लास्टिक)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "कदम 1: लॉट फोटो व तोल",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "पीछे", tint = NavyDark)
                    }
                },
                actions = {
                    VoiceIconButton(
                        onClick = {
                            val hint = "कबाड़ की फोटो खींचें, कंप्यूटर द्वारा पहचाना गया सामान जांचें और सही वजन दर्ज करें।"
                            onVoiceSpeak(hint)
                        },
                        size = 38.dp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Camera Capture / Dashed Frame Area
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (hasCapturedPhoto) NavyCard else Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { hasCapturedPhoto = !hasCapturedPhoto }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (hasCapturedPhoto) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(GreenSuccess),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = SurfaceLight,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "फोटो ली गई (Photo Captured)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SurfaceLight
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                StatusChip(
                                    text = "AI पहचान: $detectedMaterial ($confidencePercent% निश्चित)",
                                    type = ChipType.SUCCESS
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "कैमरा",
                                    tint = OrangePrimary,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "कबाड़ की फोटो खींचने के लिए टैप करें",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NavyDark
                                )
                            }
                        }
                    }
                }

                // 2. Material Recognition & Override Chip Row
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "पहचाना गया सामान:",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = detectedMaterial,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GreenLight)
                                    .clickable { isMaterialConfirmed = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "सही है ✓",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GreenDark
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(OrangeLight)
                                    .clickable { showMaterialPicker = !showMaterialPicker }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "बदलें ✎",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = OrangePrimary
                                )
                            }
                        }
                    }
                }

                // Optional material override dropdown pills
                if (showMaterialPicker) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceLight, RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        materialsList.forEach { mat ->
                            Text(
                                text = mat,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        detectedMaterial = mat
                                        confidencePercent = 99
                                        showMaterialPicker = false
                                    }
                                    .padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = if (detectedMaterial == mat) OrangePrimary else NavyDark
                            )
                        }
                    }
                }

                // 3. Weight Input Field
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "वजन दर्ज करें (Enter Weight in Kg)",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceLight)
                        .border(2.dp, OrangePrimary, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (weightInput.isEmpty()) "0.0" else weightInput,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (weightInput.isEmpty()) Color(0xFF94A3B8) else NavyDark
                            )
                        )
                        Text(
                            text = "किलोग्राम (kg)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OrangePrimary
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // 4. CTA and Custom Keypad
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KmCTAButton(
                    text = "दाम और रीसाइक्लर देखें (Get Estimate) >",
                    onClick = {
                        val weight = weightInput.toDoubleOrNull() ?: 10.0
                        val lotId = "KM-2026-0418"
                        onProceedToEstimate(lotId, weight, detectedMaterial)
                    },
                    enabled = (weightInput.toDoubleOrNull() ?: 0.0) > 0.0
                )
                Spacer(modifier = Modifier.height(6.dp))
                CustomNumericKeypad(
                    onDigitClick = { digit ->
                        if (weightInput.length < 6) {
                            if (digit == "." && weightInput.contains(".")) {
                                // Ignore multiple dots
                            } else {
                                weightInput += digit
                            }
                        }
                    },
                    onBackspaceClick = {
                        if (weightInput.isNotEmpty()) weightInput = weightInput.dropLast(1)
                    },
                    onClearClick = {
                        weightInput = ""
                    },
                    allowDecimal = true
                )
            }
        }
    }
}
