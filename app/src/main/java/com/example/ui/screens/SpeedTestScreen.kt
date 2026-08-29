package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.SpeedTestPhase
import com.example.ui.components.GlassCard
import com.example.ui.components.PulsingButton
import com.example.ui.components.StatusDot
import com.example.ui.theme.DarkBackgroundDeep
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSubtle
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedTestScreen(
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf(SpeedTestPhase.IDLE) }
    var currentSpeed by remember { mutableFloatStateOf(0f) }
    var downloadResult by remember { mutableFloatStateOf(3.2f) }
    var uploadResult by remember { mutableFloatStateOf(0.8f) }
    var pingResult by remember { mutableIntStateOf(68) }
    var jitterResult by remember { mutableIntStateOf(14) }
    val coroutineScope = rememberCoroutineScope()

    val maxDisplaySpeed = 50f
    val speedRatio = (currentSpeed / maxDisplaySpeed).coerceIn(0f, 1f)

    val startSpeedTest = {
        if (phase == SpeedTestPhase.IDLE || phase == SpeedTestPhase.FINISHED) {
            coroutineScope.launch {
                phase = SpeedTestPhase.PING
                currentSpeed = 0f
                delay(800)
                pingResult = 72
                jitterResult = 18

                // Download phase
                phase = SpeedTestPhase.DOWNLOAD
                for (i in 1..25) {
                    val progress = i / 25f
                    currentSpeed = (progress * 3.4f) + (sin(i.toDouble()) * 0.4f).toFloat()
                    delay(80)
                }
                downloadResult = 3.2f

                // Upload phase
                phase = SpeedTestPhase.UPLOAD
                for (i in 1..20) {
                    val progress = i / 20f
                    currentSpeed = (progress * 0.9f) + (sin(i.toDouble()) * 0.1f).toFloat()
                    delay(80)
                }
                uploadResult = 0.8f

                currentSpeed = 0f
                phase = SpeedTestPhase.FINISHED
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, NeonCyan.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Speed Test",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "TEST DE DÉBIT",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Serveur: MTN Brazzaville Hub 01",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(DarkSurfaceElevated, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "LTE CAT-4",
                    color = NeonBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Speedometer Circular Gauge
        Box(
            modifier = Modifier
                .size(270.dp)
                .testTag("speedometer_gauge"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerOffset = center
                val radius = size.minDimension / 2f - 20.dp.toPx()
                val startAngle = 140f
                val sweepTotalAngle = 260f

                // Track
                drawArc(
                    color = DarkSurfaceElevated,
                    startAngle = startAngle,
                    sweepAngle = sweepTotalAngle,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )

                // Active Speed Arc
                val activeSweep = sweepTotalAngle * speedRatio
                if (activeSweep > 0) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(NeonBlue, NeonCyan, NeonCyan),
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

                // Needle
                val needleAngle = startAngle + activeSweep
                val needleRad = Math.toRadians(needleAngle.toDouble())
                val needleLength = radius - 16.dp.toPx()
                val needleEnd = Offset(
                    (centerOffset.x + needleLength * cos(needleRad)).toFloat(),
                    (centerOffset.y + needleLength * sin(needleRad)).toFloat()
                )

                drawLine(
                    color = NeonCyan,
                    start = centerOffset,
                    end = needleEnd,
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawCircle(color = NeonCyan, radius = 6.dp.toPx(), center = centerOffset)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 28.dp)
            ) {
                Text(
                    text = when (phase) {
                        SpeedTestPhase.IDLE -> "PRÊT"
                        SpeedTestPhase.PING -> "PING..."
                        SpeedTestPhase.DOWNLOAD -> "TÉLÉCHARGEMENT"
                        SpeedTestPhase.UPLOAD -> "ENVOI"
                        SpeedTestPhase.FINISHED -> "TERMINÉ"
                    },
                    color = when (phase) {
                        SpeedTestPhase.DOWNLOAD, SpeedTestPhase.UPLOAD -> NeonCyan
                        SpeedTestPhase.FINISHED -> NeonAmber
                        else -> TextSecondary
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (phase == SpeedTestPhase.DOWNLOAD || phase == SpeedTestPhase.UPLOAD) {
                        String.format("%.1f", currentSpeed)
                    } else if (phase == SpeedTestPhase.FINISHED) {
                        String.format("%.1f", downloadResult)
                    } else {
                        "0.0"
                    },
                    color = TextPrimary,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Mbps",
                    color = NeonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Results Metrics 2x2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SpeedMetricCard(
                title = "DOWNLOAD",
                value = "${String.format("%.1f", downloadResult)} Mbps",
                icon = Icons.Default.ArrowDownward,
                color = NeonCyan,
                modifier = Modifier.weight(1f)
            )
            SpeedMetricCard(
                title = "UPLOAD",
                value = "${String.format("%.1f", uploadResult)} Mbps",
                icon = Icons.Default.ArrowUpward,
                color = NeonBlue,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SpeedMetricCard(
                title = "PING / LATENCE",
                value = "$pingResult ms",
                icon = Icons.Default.Timer,
                color = NeonAmber,
                modifier = Modifier.weight(1f)
            )
            SpeedMetricCard(
                title = "JITTER",
                value = "$jitterResult ms",
                icon = Icons.Default.Dns,
                color = NeonRed,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PulsingButton(
            text = if (phase == SpeedTestPhase.IDLE || phase == SpeedTestPhase.FINISHED) {
                "[ TESTER LE DÉBIT ]"
            } else {
                "TEST EN COURS..."
            },
            isLoading = phase == SpeedTestPhase.PING || phase == SpeedTestPhase.DOWNLOAD || phase == SpeedTestPhase.UPLOAD,
            onClick = { startSpeedTest() }
        )
    }
}

@Composable
private fun SpeedMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        borderColor = color.copy(alpha = 0.3f),
        backgroundColor = DarkSurfaceSubtle.copy(alpha = 0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color.copy(alpha = 0.15f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(14.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
