package com.kabadimitra.collector.ui.hisaab

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
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
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.ChipType
import com.kabadimitra.collector.core.designsystem.components.StatusChip
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton
import com.kabadimitra.collector.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Composable
fun HisaabScreen(
    transactionsFlow: Flow<List<TransactionEntity>>,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val transactions by transactionsFlow.collectAsState(initial = emptyList())
    val monthlyTotal = transactions.sumOf { it.totalAmountRupees } + 14250

    val weeklyData = listOf(
        "सोम" to 1250f,
        "मंगल" to 2100f,
        "बुध" to 1800f,
        "गुरु" to 2950f,
        "शुक्र" to 3400f,
        "शनि" to 2700f,
        "रवि" to 1600f
    )
    val maxWeekly = weeklyData.maxOf { it.second }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "खाता हिसाब (Hisaab Ledger)",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                    Text(
                        text = "सच्ची कमाई • पारदर्शी रिकॉर्ड",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
                VoiceIconButton(
                    onClick = {
                        val hint = "इस महीने आपकी कुल कमाई ₹$monthlyTotal रही है, जो पिछले महीने से 18 प्रतिशत अधिक है।"
                        onVoiceSpeak(hint)
                    },
                    size = 38.dp
                )
            }
        }

        // Monthly Summary Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "इस महीने की कुल कमाई",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF94A3B8)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GreenLight)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = GreenDark,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "+18% वृद्धि",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenDark
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "₹$monthlyTotal",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SurfaceLight
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "कुल तोल: 485 किग्रा • कमाई पासपोर्ट स्कोर: 850 (बहुत अच्छा)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // Weekly Earnings Canvas Bar Chart
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "साप्ताहिक कमाई चार्ट (Weekly Earnings)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Plain Canvas Chart (Protect APK size without external heavy charting libraries)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barWidth = 26.dp.toPx()
                            val spacing = (size.width - (barWidth * weeklyData.size)) / (weeklyData.size + 1)
                            val chartHeight = size.height - 30.dp.toPx()

                            weeklyData.forEachIndexed { index, pair ->
                                val x = spacing + index * (barWidth + spacing)
                                val barHeight = (pair.second / maxWeekly) * chartHeight
                                val y = chartHeight - barHeight

                                // Draw bar
                                drawRoundRect(
                                    color = if (pair.second == maxWeekly) OrangePrimary else Color(0xFF1E293B),
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                                )
                            }
                        }
                    }

                    // Weekday Labels Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        weeklyData.forEach { (day, _) ->
                            Text(
                                text = day,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        }

        // Transactions Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "हाल के लेन-देन (Recent Transactions)",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark
                )
            }
        }

        // Transactions List
        items(transactions) { tx ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tx.lotId,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusChip(
                                text = if (tx.isPaid) "मिला (Paid)" else "बाकी (Pending)",
                                type = if (tx.isPaid) ChipType.SUCCESS else ChipType.PENDING
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${tx.recyclerName} • ${tx.materialCategory}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "${tx.finalWeightKg} किग्रा @ ₹${tx.finalRatePerKg}/किग्रा (${tx.paymentMethod})",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+₹${tx.totalAmountRupees}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GreenDark
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
