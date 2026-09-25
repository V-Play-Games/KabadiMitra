package com.kabadimitra.collector.ui.safety

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
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Masks
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kabadimitra.collector.core.designsystem.NavyDark
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton

data class SafetyCardItem(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val description: String,
    val audioHint: String,
    val icon: ImageVector,
    val containerColor: Color,
    val accentColor: Color
)

@Composable
fun SurakshaScreen(
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val safetyCards = listOf(
        SafetyCardItem(
            id = "1",
            titleHindi = "दस्ताने और मजबूत जूते पहनें",
            titleEnglish = "Wear Protective Gloves & Boots",
            description = "नुकीले कांच, धातु या केमिकल से कटने से बचें। भारी सामान उठाते समय हमेशा मजबूत दस्ताने पहनें।",
            audioHint = "कबाड़ उठाते समय हमेशा मजबूत दस्ताने और जूते पहनें ताकि चोट और कांच से बचा जा सके।",
            icon = Icons.Default.Shield,
            containerColor = Color(0xFFFEF3C7),
            accentColor = Color(0xFFD97706)
        ),
        SafetyCardItem(
            id = "2",
            titleHindi = "धूल और धुएं से बचाव (मास्क पहनें)",
            titleEnglish = "Dust & Smoke Respiratory Safety",
            description = "कबाड़ मंडी और छंटाई के दौरान मास्क लगाएं। कभी भी प्लास्टिक को आग न लगाएं, इसका धुआं फेफड़ों के लिए जानलेवा है।",
            audioHint = "छंटाई के दौरान मास्क लगाएं। कभी भी प्लास्टिक को आग न लगाएं, इसका धुआं फेफड़ों के लिए अत्यंत खतरनाक है।",
            icon = Icons.Default.Masks,
            containerColor = Color(0xFFFEE2E2),
            accentColor = Color(0xFFDC2626)
        ),
        SafetyCardItem(
            id = "3",
            titleHindi = "हाथ धोना और स्वच्छता",
            titleEnglish = "Sanitization & Hygiene",
            description = "काम खत्म करने के बाद और खाना खाने से पहले साबुन व साफ पानी से हाथ जरूर धोएं।",
            audioHint = "काम के बाद और खाना खाने से पहले साबुन से हाथ अच्छी तरह धोएं।",
            icon = Icons.Default.CleaningServices,
            containerColor = Color(0xFFE0F2FE),
            accentColor = Color(0xFF0284C7)
        ),
        SafetyCardItem(
            id = "4",
            titleHindi = "पानी और ओआरएस पिएं (गर्मी से बचाव)",
            titleEnglish = "Hydration & Heatstroke Prevention",
            description = "धूप में काम करते समय पर्याप्त पानी पिएं। चक्कर या थकान महसूस होने पर तुरंत छाया में बैठें।",
            audioHint = "धूप में काम करते समय खूब पानी पिएं और चक्कर आने पर तुरंत छायादार जगह पर आराम करें।",
            icon = Icons.Default.WaterDrop,
            containerColor = Color(0xFFDCFCE7),
            accentColor = Color(0xFF16A34A)
        ),
        SafetyCardItem(
            id = "5",
            titleHindi = "आपातकालीन चिकित्सा व हेल्पलाइन",
            titleEnglish = "Emergency Medical Assistance",
            description = "चोट लगने पर तुरंत नजदीकी स्वास्थ्य केंद्र जाएं। आपातकालीन एम्बुलेंस सेवा: 108 पर कॉल करें।",
            audioHint = "चोट लगने पर तुरंत नजदीकी सरकारी क्लिनिक जाएं या एम्बुलेंस के लिए 108 डायल करें।",
            icon = Icons.Default.LocalHospital,
            containerColor = Color(0xFFF3E8FF),
            accentColor = Color(0xFF9333EA)
        )
    )

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
                        text = "सुरक्षा साथी (Suraksha)",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = NavyDark
                    )
                    Text(
                        text = "कचरा बीनने वाले साथियों की सुरक्षा नियम",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
                VoiceIconButton(
                    onClick = {
                        val allIntro = "सुरक्षा साथी में आपका स्वागत है। किसी भी कार्ड का स्पीकर दबाकर सुरक्षा निर्देश अपनी भाषा में सुनें।"
                        onVoiceSpeak(allIntro)
                    },
                    size = 38.dp
                )
            }
        }

        // Safety Cards
        items(safetyCards) { card ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = card.containerColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVoiceSpeak(card.audioHint) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(card.accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = card.icon,
                            contentDescription = null,
                            tint = SurfaceLight,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = card.titleHindi,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NavyDark
                        )
                        Text(
                            text = card.titleEnglish,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = card.accentColor
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = card.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF334155)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    VoiceIconButton(
                        onClick = { onVoiceSpeak(card.audioHint) },
                        size = 36.dp,
                        tint = card.accentColor,
                        backgroundColor = SurfaceLight
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
