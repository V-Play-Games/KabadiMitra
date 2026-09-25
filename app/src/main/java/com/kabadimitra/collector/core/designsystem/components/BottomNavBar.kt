package com.kabadimitra.collector.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary

enum class KmBottomTab(
    val route: String,
    val hindiTitle: String,
    val englishTitle: String,
    val icon: ImageVector
) {
    HOME("home", "होम", "Home", Icons.Default.Home),
    BHAV("priceboard", "भाव बोर्ड", "Rates", Icons.Default.CurrencyRupee),
    HISAAB("hisaab", "हिसाब", "Ledger", Icons.Default.MenuBook),
    SURAKSHA("safety", "सुरक्षा", "Safety", Icons.Default.Security)
}

@Composable
fun KmBottomNavBar(
    selectedTab: KmBottomTab,
    onTabSelected: (KmBottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KmBottomTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val contentColor = if (isSelected) OrangePrimary else Color(0xFF64748B)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) OrangeLight else Color.Transparent)
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = "${tab.hindiTitle} / ${tab.englishTitle}",
                            tint = contentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = tab.hindiTitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = contentColor
                    )
                }
            }
        }
    }
}
