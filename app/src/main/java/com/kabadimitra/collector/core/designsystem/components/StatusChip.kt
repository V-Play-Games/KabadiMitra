package com.kabadimitra.collector.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kabadimitra.collector.core.designsystem.GreenDark
import com.kabadimitra.collector.core.designsystem.GreenLight
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.AmberAlert
import com.kabadimitra.collector.core.designsystem.AmberLight

enum class ChipType {
    SUCCESS,  // e.g. "मिला", "सत्यापित", "CPCB प्रमाणित"
    PENDING,  // e.g. "बाकी", "ऑफ़र मिला"
    WARNING,  // e.g. "जलाने का नुकसान"
    NEUTRAL   // e.g. "ड्राफ्ट", "PET बॉटल"
}

@Composable
fun StatusChip(
    text: String,
    type: ChipType = ChipType.NEUTRAL,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when (type) {
        ChipType.SUCCESS -> Triple(GreenLight, GreenDark, Color(0xFF86EFAC))
        ChipType.PENDING -> Triple(OrangeLight, OrangePrimary, Color(0xFFFDBA74))
        ChipType.WARNING -> Triple(AmberLight, AmberAlert, Color(0xFFFCD34D))
        ChipType.NEUTRAL -> Triple(Color(0xFFF1F5F9), Color(0xFF475569), Color(0xFFCBD5E1))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            ),
            color = textColor
        )
    }
}
