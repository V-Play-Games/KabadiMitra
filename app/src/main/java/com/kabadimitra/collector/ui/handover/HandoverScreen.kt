package com.kabadimitra.collector.ui.handover

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.kabadimitra.collector.core.designsystem.components.KmCTAButton
import com.kabadimitra.collector.core.designsystem.components.StatusChip
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandoverScreen(
    lotId: String,
    otpCode: String = "4812",
    onNavigateBack: () -> Unit,
    onCompleteHandover: (lotId: String) -> Unit,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "पक्की पर्ची हैंडओवर (Handover)",
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
                            val hint = "यह क्यूआर कोड खरीदार को स्कैन करने दें या चार अंकों का ओटीपी $otpCode बताएं।"
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Lot ID Chip & Ed25519 Signed Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(text = "लॉट ID: $lotId", type = ChipType.NEUTRAL)
                    StatusChip(text = "Ed25519 डिजिटल हस्ताक्षरित ✓", type = ChipType.SUCCESS)
                }

                // QR Code Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFFCBD5E1), RoundedCornerShape(20.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "रीसाइक्लर को स्कैन कराएं",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyDark
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // High-contrast QR Pattern Drawn on Canvas
                        Box(
                            modifier = Modifier
                                .size(210.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(2.dp, NavyDark, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val gridSize = 13
                                val cellSize = size.width / gridSize
                                val darkColor = Color(0xFF0F172A)

                                // Draw simulated structured QR code matrix
                                for (row in 0 until gridSize) {
                                    for (col in 0 until gridSize) {
                                        // Corner anchors
                                        val isCornerTL = row < 4 && col < 4
                                        val isCornerTR = row < 4 && col >= gridSize - 4
                                        val isCornerBL = row >= gridSize - 4 && col < 4

                                        val shouldFill = when {
                                            isCornerTL || isCornerTR || isCornerBL -> {
                                                // Outer border or inner center of anchor
                                                val r = if (row >= gridSize - 4) row - (gridSize - 4) else row
                                                val c = if (col >= gridSize - 4) col - (gridSize - 4) else col
                                                (r == 0 || r == 3 || c == 0 || c == 3) || (r in 1..2 && c in 1..2)
                                            }
                                            (row + col * 3) % 4 == 0 -> true
                                            (row * 2 + col) % 5 == 1 -> true
                                            else -> false
                                        }

                                        if (shouldFill) {
                                            drawRect(
                                                color = darkColor,
                                                topLeft = Offset(col * cellSize, row * cellSize),
                                                size = Size(cellSize - 1f, cellSize - 1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "सत्यापित डेटा: वजन, दर, GPS स्थान",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = GreenDark
                            )
                        }
                    }
                }

                // 4-Digit Backup OTP Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = OrangeLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "या खरीदार को 4-अंकों का OTP बताएं:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                            VoiceIconButton(
                                onClick = {
                                    val digits = otpCode.map { "$it " }.joinToString("")
                                    onVoiceSpeak("ओटीपी है: $digits")
                                },
                                size = 36.dp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            otpCode.forEach { char ->
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceLight)
                                        .border(2.dp, OrangePrimary, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = char.toString(),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = OrangePrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Handover Complete Confirmation CTA
            KmCTAButton(
                text = "हैंडओवर पूरा हुआ • रसीद देखें >",
                onClick = { onCompleteHandover(lotId) },
                trailingIcon = Icons.Default.Check,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
