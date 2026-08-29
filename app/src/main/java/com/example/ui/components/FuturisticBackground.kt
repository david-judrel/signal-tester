package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBackgroundDeep
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan

@Composable
fun FuturisticBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Deep background gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        DarkBackgroundDeep,
                        DarkBackground
                    )
                )
            )

            // Top Cyan & Blue RF Radial Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = glowAlpha),
                        NeonBlue.copy(alpha = glowAlpha * 0.5f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.5f, height * 0.15f),
                    radius = width * 0.7f
                ),
                radius = width * 0.7f,
                center = Offset(width * 0.5f, height * 0.15f)
            )

            // Bottom subtle grid lines (Tech blueprint aesthetic)
            val gridSpacing = 40.dp.toPx()
            var x = 0f
            while (x < width) {
                drawLine(
                    color = Color(0x0A38BDF8),
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1f
                )
                x += gridSpacing
            }

            var y = 0f
            while (y < height) {
                drawLine(
                    color = Color(0x0A38BDF8),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
                y += gridSpacing
            }
        }

        content()
    }
}
