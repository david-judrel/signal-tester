package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SurveyPoint
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusDot
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
fun SurveyModeScreen(
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var currentDbm by remember { mutableStateOf(-102) }
    val points = remember {
        mutableStateListOf(
            SurveyPoint("1", 0.2f, 0.3f, -82, "4G LTE", "Entrée Principale", "14:20"),
            SurveyPoint("2", 0.35f, 0.4f, -92, "4G LTE", "Couloir A", "14:22"),
            SurveyPoint("3", 0.5f, 0.55f, -102, "4G LTE", "Bureau Central", "14:25"),
            SurveyPoint("4", 0.65f, 0.7f, -108, "4G LTE", "Salle Serveur", "14:27")
        )
    }

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
                        imageVector = Icons.Default.AddLocation,
                        contentDescription = "Survey Mode",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "MODE SURVEY / WALK-TEST",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Cartographie RF Intérieure/Extérieure",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(if (isRecording) NeonRed.copy(alpha = 0.2f) else DarkSurfaceElevated, RoundedCornerShape(8.dp))
                    .border(1.dp, if (isRecording) NeonRed else Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusDot(color = if (isRecording) NeonRed else TextSecondary, size = 6.dp, pulsing = isRecording)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isRecording) "REC EN COURS" else "VEILLE",
                        color = if (isRecording) NeonRed else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Survey Trail Floorplan Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurfaceSubtle)
                .border(1.dp, NeonCyan.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                .testTag("survey_canvas_trail")
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Floor grid
                val step = 28.dp.toPx()
                var x = 0f
                while (x < width) {
                    drawLine(color = Color(0x1038BDF8), start = Offset(x, 0f), end = Offset(x, height), strokeWidth = 1f)
                    x += step
                }
                var y = 0f
                while (y < height) {
                    drawLine(color = Color(0x1038BDF8), start = Offset(0f, y), end = Offset(width, y), strokeWidth = 1f)
                    y += step
                }

                // Draw connecting path line between points
                for (i in 0 until points.size - 1) {
                    val p1 = points[i]
                    val p2 = points[i + 1]
                    drawLine(
                        color = NeonBlue.copy(alpha = 0.5f),
                        start = Offset(p1.xRatio * width, p1.yRatio * height),
                        end = Offset(p2.xRatio * width, p2.yRatio * height),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Draw survey breadcrumb points
                points.forEach { pt ->
                    val dotColor = if (pt.dbm > -85) NeonCyan else if (pt.dbm > -100) NeonAmber else NeonRed
                    val centerPt = Offset(pt.xRatio * width, pt.yRatio * height)
                    drawCircle(color = dotColor.copy(alpha = 0.3f), radius = 14.dp.toPx(), center = centerPt)
                    drawCircle(color = dotColor, radius = 6.dp.toPx(), center = centerPt)
                    drawCircle(color = Color.White, radius = 2.dp.toPx(), center = centerPt)
                }
            }

            Text(
                text = "PLAN D'ÉTAGE VIRTUEL",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action controls for survey
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { isRecording = !isRecording },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) NeonRed else NeonCyan,
                    contentColor = Color(0xFF080D1A)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isRecording) "ARRÊTER" else "DÉMARRER", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = {
                    points.add(
                        SurveyPoint(
                            id = "${points.size + 1}",
                            xRatio = (0.3f + Math.random() * 0.4f).toFloat(),
                            yRatio = (0.3f + Math.random() * 0.4f).toFloat(),
                            dbm = -102,
                            networkType = "4G LTE",
                            note = "Point #${points.size + 1}",
                            timestamp = "14:30"
                        )
                    )
                },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue)
            ) {
                Icon(imageVector = Icons.Default.Flag, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "MARQUER POINT", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log list of recorded points
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(18.dp),
            borderColor = NeonBlue.copy(alpha = 0.3f),
            backgroundColor = DarkSurfaceElevated.copy(alpha = 0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(text = "POINTS MARQUÉS (${points.size})", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                points.forEach { pt ->
                    val color = if (pt.dbm > -85) NeonCyan else if (pt.dbm > -100) NeonAmber else NeonRed
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusDot(color = color, size = 6.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = pt.note, color = TextPrimary, fontSize = 12.sp)
                        }
                        Text(text = "${pt.dbm} dBm", color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}
