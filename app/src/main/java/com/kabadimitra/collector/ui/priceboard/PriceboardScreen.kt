package com.kabadimitra.collector.ui.priceboard

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.kabadimitra.collector.core.designsystem.NavyDark
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight
import com.kabadimitra.collector.core.designsystem.components.TrendArrow
import com.kabadimitra.collector.core.designsystem.components.VoiceIconButton
import com.kabadimitra.collector.data.local.entities.PriceEntry
import kotlinx.coroutines.flow.Flow

@Composable
fun PriceboardScreen(
    ratesFlow: Flow<List<PriceEntry>>,
    onVoiceSpeak: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allRates by ratesFlow.collectAsState(initial = emptyList())
    var selectedCategory by remember { mutableStateOf("सभी (All)") }

    val categories = listOf("सभी (All)", "Plastics", "Metals", "Paper")
    val filteredRates = if (selectedCategory == "सभी (All)") {
        allRates
    } else {
        allRates.filter { it.category.contains(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header with Voice Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "मंडी भाव बोर्ड",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = NavyDark
                )
                Text(
                    text = "दैनिक आधिकारिक रीसाइक्लिंग दरें",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
            }
            VoiceIconButton(
                onClick = {
                    val summary = "आज मंडी में प्लास्टिक 18 रुपये, लोहा 32 रुपये और तांबा 460 रुपये प्रति किलो चल रहा है।"
                    onVoiceSpeak(summary)
                }
            )
        }

        // Category Pills
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) NavyDark else SurfaceLight)
                        .border(
                            1.dp,
                            if (isSelected) NavyDark else Color(0xFFCBD5E1),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) SurfaceLight else NavyDark
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rates List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredRates) { price ->
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
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = price.hindiName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NavyDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${price.material} • ${price.area}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "14-दिन औसत दर: ₹${price.median14DaysRate}/किग्रा",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${price.ratePerKg}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NavyDark
                                )
                            )
                            Text(
                                text = "प्रति किग्रा",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            TrendArrow(diffRupees = price.rateDiff)
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
