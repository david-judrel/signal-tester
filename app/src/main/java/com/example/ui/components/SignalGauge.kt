package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SignalQualityLevel
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SignalGauge(
    signalDbm: Int,
    statusLevel: SignalQualityLevel,
    isScanning: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Normalizing dBm (-140 to -44 dBm) -> progress (0.0 to 1.0)
    // -140 dBm is 0%, -44 dBm is 100%
    val targetProgress = ((signalDbm - (-140f)) / (-44f - (-140f))).coerceIn(0.05f, 1f)
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(targetProgress) {
        animatedProgress.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    // Scanning radar line rotation
    val infiniteTransition = rememberInfiniteTransition(label = "RadarSweep")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SweepAngle"
    )

    // Glow pulse for the active neon value
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowIntensity"
    )

    val activeColor = statusLevel.color

    Box(
        modifier = modifier
            .size(280.dp)
            .testTag("signal_gauge_container"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = center
            val radius = size.minDimension / 2f - 24.dp.toPx()
            val startAngle = 135f
            val sweepTotalAngle = 270f

            // 1. Background Arc Track (Dark subtle tech track)
            drawArc(
                color = DarkSurface.copy(alpha = 0.7f),
                startAngle = startAngle,
                sweepAngle = sweepTotalAngle,
                useCenter = false,
                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )

            // 2. Active Gradient Arc
            val activeSweep = sweepTotalAngle * animatedProgress.value
            if (activeSweep > 0) {
                // Outer glow shadow
                drawArc(
                    color = activeColor.copy(alpha = 0.25f * glowIntensity),
                    startAngle = startAngle,
                    sweepAngle = activeSweep,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
                )

                // Main vibrant arc
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            NeonRed,
                            NeonAmber,
                            NeonCyan,
                            activeColor
                        ),
                        center = centerOffset
                    ),
                    startAngle = startAngle,
                    sweepAngle = activeSweep,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // 3. Technical Radial Tick Marks
            val numTicks = 24
            val innerTickRadius = radius - 16.dp.toPx()
            val outerTickRadius = radius - 8.dp.toPx()
            for (i in 0..numTicks) {
                val tickFraction = i.toFloat() / numTicks
                val angleDeg = startAngle + tickFraction * sweepTotalAngle
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val isMajor = i % 6 == 0
                val tickColor = if (tickFraction <= animatedProgress.value) {
                    activeColor.copy(alpha = if (isMajor) 0.9f else 0.5f)
                } else {
                    Color.White.copy(alpha = if (isMajor) 0.25f else 0.1f)
                }
                val startX = (centerOffset.x + (innerTickRadius - (if (isMajor) 4.dp.toPx() else 0f)) * cos(angleRad)).toFloat()
                val startY = (centerOffset.y + (innerTickRadius - (if (isMajor) 4.dp.toPx() else 0f)) * sin(angleRad)).toFloat()
                val endX = (centerOffset.x + outerTickRadius * cos(angleRad)).toFloat()
                val endY = (centerOffset.y + outerTickRadius * sin(angleRad)).toFloat()

                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 4. Scanning Radar Sweep effect if scanning
            if (isScanning) {
                val sweepRad = Math.toRadians(sweepAngle.toDouble())
                val sweepEndX = (centerOffset.x + (radius - 4.dp.toPx()) * cos(sweepRad)).toFloat()
                val sweepEndY = (centerOffset.y + (radius - 4.dp.toPx()) * sin(sweepRad)).toFloat()
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(NeonCyan.copy(alpha = 0.9f), Color.Transparent),
                        start = centerOffset,
                        end = Offset(sweepEndX, sweepEndY)
                    ),
                    start = centerOffset,
                    end = Offset(sweepEndX, sweepEndY),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // Inner subtle dotted ring
            drawCircle(
                color = activeColor.copy(alpha = 0.12f),
                radius = radius - 32.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Center Metric Readout Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            // Ultra-large 70sp text showing "-102 dBm" with sleek styling
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.testTag("signal_value_row")
            ) {
                Text(
                    text = "$signalDbm",
                    color = Color.White,
                    fontSize = 62.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-2).sp,
                    modifier = Modifier.testTag("signal_value_text")
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "dBm",
                    color = TextSecondary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .testTag("signal_unit_text")
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Pulse dot + Status Label: FAIBLE in Neon Red (#EF4444)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("signal_status_badge")
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(activeColor, androidx.compose.foundation.shape.CircleShape)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = statusLevel.labelFr.uppercase(),
                    color = activeColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )
            }
        }
    }
}

