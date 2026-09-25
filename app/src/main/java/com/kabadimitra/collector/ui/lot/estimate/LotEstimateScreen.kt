package com.kabadimitra.collector.ui.lot.estimate

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
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kabadimitra.collector.core.designsystem.AmberAlert
import com.kabadimitra.collector.core.designsystem.AmberLight
import com.kabadimitra.collector.core.designsystem.GreenDark
import com.kabadimitra.collector.core.designsystem.GreenLight
import com.kabadimitra.collector.core.designsystem.GreenSuccess
import com.kabadimitra.collector.core.designsystem.NavyCard
import com.kabadimitra.collector.core.designsystem.NavyDark
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.RedDanger
import com.kabadimitra.collector.core.designsystem.RedLight
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.ChipType
import com.kabadimitra.collector.core.designsystem.components.KmButtonVariant
import com.kabadimitra.collector.core.designsystem.components.KmCTAButton
import com.kabadimitra.collector.core.designsystem.components.StatusChip
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotEstimateScreen(
    lotId: String,
    weightKg: Double = 28.5,
    material: String = "PET Plastic",
    ratePerKg: Int = 18,
    onNavigateBack: () -> Unit,
    onProceedToBuyers: (lotId: String) -> Unit,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estimatedAmount = (weightKg * ratePerKg).toInt()
    val jalaoLoss = estimatedAmount // Burning plastic yields ₹0 and high pollution fines

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "कदम 2: अनुमानित भाव व कमाई",
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
                            val hint = "आपके ${weightKg} किलो प्लास्टिक का अनुमानित मूल्य ₹${estimatedAmount} है। रीसाइक्लिंग से पूरी कमाई होती है, जलाने पर नुकसान और जुर्माना होता है।"
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Summary Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusChip(text = material, type = ChipType.NEUTRAL)
                    StatusChip(text = "$weightKg किग्रा", type = ChipType.NEUTRAL)
                    StatusChip(text = "दर: ₹$ratePerKg/किग्रा", type = ChipType.SUCCESS)
                }

                // Big Rupee Value Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "अनुमानित कुल कमाई (Estimated Payout)",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenSuccess
                                )
                            )
                            Text(
                                text = "$estimatedAmount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SurfaceLight
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "समीकरण: $weightKg किग्रा × ₹$ratePerKg (14-दिन औसत दर)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }

                // "Jalao Mat, Kamao" Dual-Bar Comparison Card
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
                            text = "जलाओ मत, कमाओ! (Recycle vs Burn)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyDark
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Option 1: Sahi Recycler (Green Bar)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "सही खरीदार को बेचें (Green Recycle)",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = GreenDark
                            )
                            Text(
                                text = "+₹$estimatedAmount",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenDark
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { 1.0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = GreenSuccess,
                            trackColor = GreenLight
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Option 2: Burning (Red Bar)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "खुले में जलाना (Burn & Waste)",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = RedDanger
                            )
                            Text(
                                text = "₹0 + स्वास्थ्य व प्रदूषण नुकसान",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RedDanger
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { 0.05f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = RedDanger,
                            trackColor = RedLight
                        )
                    }
                }

                // Warning / Incentive Banner
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AmberLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AmberAlert,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "CPCB अधिकृत रीसाइक्लर को देने से आपको ₹2/किग्रा का अतिरिक्त बोनस और पक्की पर्ची मिलती है।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = AmberAlert
                            )
                        )
                    }
                }

                // Editable GPS Pickup Card
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(OrangeLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "पिकअप स्थान (GPS Match):",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "धारावी 90 फीट रोड, मुंबई (19.0434° N, 72.8562° E)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button to Buyers
            KmCTAButton(
                text = "सही खरीदार चुनें (Select Buyer) >",
                onClick = { onProceedToBuyers(lotId) },
                trailingIcon = Icons.Default.ArrowForward,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
