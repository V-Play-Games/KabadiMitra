package com.kabadimitra.collector.ui.receipt

import android.content.Intent
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
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
import com.kabadimitra.collector.core.designsystem.components.KmButtonVariant
import com.kabadimitra.collector.core.designsystem.components.KmCTAButton
import com.kabadimitra.collector.core.designsystem.components.StatusChip
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    lotId: String,
    recyclerName: String = "EcoGreen Polymers",
    material: String = "PET Plastic",
    finalWeightKg: Double = 28.5,
    finalRatePerKg: Int = 20,
    paymentMethod: String = "UPI",
    onNavigateToHisaab: () -> Unit,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val totalAmount = (finalWeightKg * finalRatePerKg).toInt()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "पक्की पर्ची (Verified Receipt)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                },
                actions = {
                    VoiceIconButton(
                        onClick = {
                            val hint = "लेन-देन सफल रहा! कुल ₹$totalAmount प्राप्त हुए। दोनों पक्षों द्वारा डिजिटल हस्ताक्षर सत्यापित हैं।"
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
                // Success Badge & Icon
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(GreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GreenSuccess,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Text(
                    text = "लेन-देन सफल! (Payment Confirmed)",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark
                )

                // Total Rupee Payout Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "कुल प्राप्त राशि",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "₹$totalAmount",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GreenDark
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        StatusChip(
                            text = "भुगतान विधि: $paymentMethod (सत्यापित)",
                            type = ChipType.SUCCESS
                        )
                    }
                }

                // Breakdown Receipt Table Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "रसीद विवरण (Receipt Details)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyDark
                        )
                        HorizontalDivider(color = Color(0xFFE2E8F0))

                        ReceiptDetailRow(label = "लॉट क्रमांक (Lot ID)", value = lotId)
                        ReceiptDetailRow(label = "खरीदार (Recycler)", value = recyclerName)
                        ReceiptDetailRow(label = "सामग्री (Material)", value = material)
                        ReceiptDetailRow(label = "अंतिम वजन (Final Weight)", value = "$finalWeightKg किग्रा")
                        ReceiptDetailRow(label = "तय दर (Rate per kg)", value = "₹$finalRatePerKg/किग्रा")
                        ReceiptDetailRow(label = "GPS दूरी मिलान", value = "12 मीटर (सत्यापित)")

                        HorizontalDivider(color = Color(0xFFE2E8F0))

                        // Dual Signature Trust Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GreenLight, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "दोनों पक्षों द्वारा डिजिटल रूप से हस्ताक्षरित (Dual Ed25519 Signatures Verified)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenDark
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Share & View Hisaab
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KmCTAButton(
                    text = "पक्की पर्ची शेयर करें (WhatsApp / SMS)",
                    onClick = {
                        val shareText = "कबाड़ मित्र पक्की पर्ची\nलॉट: $lotId\nसामग्री: $material ($finalWeightKg किग्रा)\nदर: ₹$finalRatePerKg/किग्रा\nकुल राशि: ₹$totalAmount ($paymentMethod)\nरीसाइक्लर: $recyclerName"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "पर्ची शेयर करें"))
                    },
                    variant = KmButtonVariant.OUTLINED,
                    leadingIcon = Icons.Default.Share
                )

                KmCTAButton(
                    text = "हिसाब देखें (Go to Ledger) >",
                    onClick = onNavigateToHisaab,
                    variant = KmButtonVariant.PRIMARY_ORANGE,
                    trailingIcon = Icons.Default.MenuBook
                )
            }
        }
    }
}

@Composable
private fun ReceiptDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = NavyDark
        )
    }
}
