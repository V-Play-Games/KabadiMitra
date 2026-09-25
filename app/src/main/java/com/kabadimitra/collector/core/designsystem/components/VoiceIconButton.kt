package com.kabadimitra.collector.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kabadimitra.collector.core.designsystem.OrangeLight
import com.kabadimitra.collector.core.designsystem.OrangePrimary

@Composable
fun VoiceIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    contentDescription: String = "बोलकर सुनें (Tap to hear audio)",
    size: Dp = 44.dp,
    tint: Color = OrangePrimary,
    backgroundColor: Color = OrangeLight
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isPlaying) 1.18f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val animatedBg by animateColorAsState(
        targetValue = if (isPlaying) OrangePrimary.copy(alpha = 0.25f) else backgroundColor,
        label = "bgColor"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(if (isPlaying) pulseScale else 1.0f)
            .clip(CircleShape)
            .background(animatedBg)
            .border(
                width = if (isPlaying) 2.dp else 1.dp,
                color = if (isPlaying) OrangePrimary else OrangePrimary.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
