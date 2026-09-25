package com.kabadimitra.collector.ui.buyers

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.collectAsState
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
import com.kabadimitra.collector.core.designsystem.NavyDark
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.ChipType
import com.kabadimitra.collector.core.designsystem.components.KmCTAButton
import com.kabadimitra.collector.core.designsystem.components.StatusChip
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton
import com.kabadimitra.collector.data.local.entities.RecyclerEntity
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyersScreen(
    lotId: String,
    recyclersFlow: Flow<List<RecyclerEntity>>,
    onNavigateBack: () -> Unit,
    onRecyclerSelected: (recyclerId: String) -> Unit,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val recyclers by recyclersFlow.collectAsState(initial = emptyList())
    var selectedRecyclerId by remember { mutableStateOf("REC-MUM-01") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "सही खरीदार (Sahi Kharidar)",
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
                            val hint = "निकटतम CPCB प्रमाणित खरीदार चुनें। शीर्ष खरीदार इको-ग्रीन ₹2 अतिरिक्त बोनस दे रहे हैं।"
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Subtitle
                item {
                    Text(
                        text = "उच्चतम भुगतान और प्रमाणित रीसाइक्लर (Ranked by net payout)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }

                // Ranked Recycler Cards
                items(recyclers) { recycler ->
                    val isSelected = recycler.id == selectedRecyclerId

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) OrangePrimary else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedRecyclerId = recycler.id }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Top Row: Badges & Select Radio
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (recycler.cpcbCertified) {
                                        StatusChip(text = "CPCB प्रमाणित", type = ChipType.SUCCESS)
                                    }
                                    if (recycler.baseRateBonusPerKg > 0) {
                                        StatusChip(
                                            text = "+₹${recycler.baseRateBonusPerKg} सर्वश्रेष्ठ दर",
                                            type = ChipType.PENDING
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .border(
                                            2.dp,
                                            if (isSelected) OrangePrimary else Color(0xFFCBD5E1),
                                            CircleShape
                                        )
                                        .background(if (isSelected) OrangePrimary else Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(SurfaceLight)
                                        )
                                    }
                                }
                            }

                            // Recycler Name & Distance
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = recycler.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${recycler.area} (${recycler.distanceKm} किमी)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${recycler.rating} (${recycler.verifiedLotsCount}+ तोल)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = NavyDark
                                )
                            }

                            // Pickup slot
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = GreenDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "पिकअप समय: ${recycler.pickupSlot}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = GreenDark
                                )
                            }
                        }
                    }
                }

                // Bottom Pooling Banner (PRD P1 / Stretch Feature)
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GreenSuccess),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = SurfaceLight,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "पूलिंग विकल्प (Neighborhood Pooling)",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GreenDark
                                )
                                Text(
                                    text = "आसपास के 2 अन्य कबाड़ी मिलकर पिकअप मंगा रहे हैं। वाहन खर्च ₹0!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GreenDark
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Confirm Selection Button
            KmCTAButton(
                text = "हैंडओवर और क्यूआर कोड बनाएं (Generate QR) >",
                onClick = { onRecyclerSelected(selectedRecyclerId) },
                trailingIcon = Icons.Default.ArrowForward,
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            )
        }
    }
}
