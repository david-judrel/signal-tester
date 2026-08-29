package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
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

@Composable
fun CoverageMapScreen(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarMap")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RadarPulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
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
                        imageVector = Icons.Default.Map,
                        contentDescription = "Coverage Map",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "CARTE DE COUVERTURE",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Brazzaville Centre • Tour MTN #1234",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(DarkSurfaceElevated, RoundedCornerShape(8.dp))
                    .border(1.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "GPS Fix",
                        tint = NeonCyan,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "GPS ±3m",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Futuristic Radar Grid Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurfaceSubtle)
                .border(1.dp, NeonCyan.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                .testTag("coverage_radar_map")
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val centerOffset = Offset(width * 0.5f, height * 0.48f)

                // Background tech grid
                val step = 32.dp.toPx()
                var x = 0f
                while (x < width) {
                    drawLine(
                        color = Color(0x1438BDF8),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1f
                    )
                    x += step
                }
                var y = 0f
                while (y < height) {
                    drawLine(
                        color = Color(0x1438BDF8),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                    y += step
                }

                // Coverage concentric rings
                val maxRadius = width * 0.42f
                val ringCounts = 4
                for (i in 1..ringCounts) {
                    val r = (maxRadius / ringCounts) * i
                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.15f),
                        radius = r,
                        center = centerOffset,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Radiating pulse wave
                drawCircle(
                    color = NeonCyan.copy(alpha = (1f - radarPulse) * 0.4f),
                    radius = maxRadius * radarPulse,
                    center = centerOffset,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Crosshairs
                drawLine(
                    color = NeonCyan.copy(alpha = 0.25f),
                    start = Offset(centerOffset.x - maxRadius, centerOffset.y),
                    end = Offset(centerOffset.x + maxRadius, centerOffset.y),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = NeonCyan.copy(alpha = 0.25f),
                    start = Offset(centerOffset.x, centerOffset.y - maxRadius),
                    end = Offset(centerOffset.x, centerOffset.y + maxRadius),
                    strokeWidth = 1.dp.toPx()
                )

                // Cell Tower Position (Center)
                drawCircle(color = NeonCyan, radius = 6.dp.toPx(), center = centerOffset)

                // Neighbor towers / Weak spots
                val userLocation = Offset(width * 0.72f, height * 0.68f)
                val weakSpot = Offset(width * 0.32f, height * 0.35f)
                val goodSpot = Offset(width * 0.48f, height * 0.22f)

                // Weak signal zone aura (Red)
                drawCircle(
                    color = NeonRed.copy(alpha = 0.25f),
                    radius = 35.dp.toPx(),
                    center = userLocation
                )
                // User location dot (Current: FAIBLE -102 dBm)
                drawCircle(color = NeonRed, radius = 7.dp.toPx(), center = userLocation)

                // Tower connecting line
                drawLine(
                    color = NeonRed.copy(alpha = 0.6f),
                    start = centerOffset,
                    end = userLocation,
                    strokeWidth = 1.5.dp.toPx()
                )

                // Neighbor good spot (Cyan)
                drawCircle(color = NeonCyan.copy(alpha = 0.2f), radius = 28.dp.toPx(), center = goodSpot)
                drawCircle(color = NeonCyan, radius = 5.dp.toPx(), center = goodSpot)
            }

            // Overlay legend on top of map
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .background(DarkBackgroundDeep.copy(alpha = 0.85f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusDot(color = NeonRed, size = 6.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Votre position (-102 dBm)", color = TextPrimary, fontSize = 10.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusDot(color = NeonCyan, size = 6.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Antenne relais eNodeB #1234", color = TextSecondary, fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tower & Radio Site Specs Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(18.dp),
            borderColor = NeonBlue.copy(alpha = 0.35f),
            backgroundColor = DarkSurfaceElevated.copy(alpha = 0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SITE RADIO & POSITION",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Azimut: 142° SE",
                        color = NeonBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Distance Antenne", color = TextSecondary, fontSize = 11.sp)
                        Text(text = "1.42 km", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                    Column {
                        Text(text = "Coordonnées", color = TextSecondary, fontSize = 11.sp)
                        Text(text = "4°15'42\" S, 15°16'58\" E", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}
