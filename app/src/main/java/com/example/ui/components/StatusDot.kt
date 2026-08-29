package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StatusDot(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 12.dp,
    pulsing: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DotPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (pulsing) 1.8f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = if (pulsing) 0f else 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = modifier.size(size * 2),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 2)) {
            val centerOffset = center
            val baseRadius = size.toPx() / 2f

            if (pulsing) {
                // Outer expanding ripple ring
                drawCircle(
                    color = color.copy(alpha = pulseAlpha),
                    radius = baseRadius * pulseScale,
                    center = centerOffset,
                    style = Stroke(width = 2f)
                )
            }

            // Glow aura
            drawCircle(
                color = color.copy(alpha = 0.35f),
                radius = baseRadius * 1.35f,
                center = centerOffset
            )

            // Inner solid bright dot
            drawCircle(
                color = color,
                radius = baseRadius,
                center = centerOffset
            )
        }
    }
}
