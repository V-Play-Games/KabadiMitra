package com.kabadimitra.collector.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kabadimitra.collector.core.designsystem.GreenDark
import com.kabadimitra.collector.core.designsystem.GreenLight
import com.kabadimitra.collector.core.designsystem.RedDanger
import com.kabadimitra.collector.core.designsystem.RedLight

@Composable
fun TrendArrow(
    diffRupees: Int,
    modifier: Modifier = Modifier
) {
    val isPositive = diffRupees > 0
    val isNegative = diffRupees < 0

    val bgColor = when {
        isPositive -> GreenLight
        isNegative -> RedLight
        else -> Color(0xFFF1F5F9)
    }

    val contentColor = when {
        isPositive -> GreenDark
        isNegative -> RedDanger
        else -> Color(0xFF64748B)
    }

    val icon = when {
        isPositive -> Icons.Default.ArrowUpward
        isNegative -> Icons.Default.ArrowDownward
        else -> Icons.Default.Remove
    }

    val label = when {
        isPositive -> "+₹$diffRupees"
        isNegative -> "-₹${Math.abs(diffRupees)}"
        else -> "स्थिर"
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
