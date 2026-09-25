package com.kabadimitra.collector.core.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kabadimitra.collector.core.designsystem.OrangePrimary
import com.kabadimitra.collector.core.designsystem.SurfaceLight

enum class KmButtonVariant {
    PRIMARY_ORANGE,
    NAVY_SOLID,
    GREEN_VERIFIED,
    OUTLINED
}

@Composable
fun KmCTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: KmButtonVariant = KmButtonVariant.PRIMARY_ORANGE,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    fullWidth: Boolean = true
) {
    val (containerColor, contentColor) = when (variant) {
        KmButtonVariant.PRIMARY_ORANGE -> OrangePrimary to SurfaceLight
        KmButtonVariant.NAVY_SOLID -> MaterialTheme.colorScheme.primary to SurfaceLight
        KmButtonVariant.GREEN_VERIFIED -> Color(0xFF16A34A) to SurfaceLight
        KmButtonVariant.OUTLINED -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.primary
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .heightIn(min = 52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledContentColor = Color(0xFF64748B)
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
