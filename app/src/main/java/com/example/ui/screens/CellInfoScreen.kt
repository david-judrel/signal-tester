package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NeighborCell
import com.example.model.SignalMetrics
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusDot
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSubtle
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun CellInfoScreen(
    modifier: Modifier = Modifier
) {
    val metrics = remember { SignalMetrics() }
    val neighborCells = remember {
        listOf(
            NeighborCell("12345678", 246, "B3 (1800)", -102, -14, 1420, isConnected = true),
            NeighborCell("12345680", 112, "B3 (1800)", -108, -16, 2100, isConnected = false),
            NeighborCell("12345672", 89, "B7 (2600)", -112, -18, 2800, isConnected = false),
            NeighborCell("12345695", 305, "B20 (800)", -96, -12, 1950, isConnected = false)
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
                        imageVector = Icons.Default.CellTower,
                        contentDescription = "Cell Info",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "CELL INFO DÉTAILLÉ",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "eNodeB Secteur & Télémétrie RF",
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
                Text(
                    text = "LTE REL-15",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Serving Cell Detailed Telemetry Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            borderColor = NeonCyan.copy(alpha = 0.35f),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Radio, contentDescription = "Serving Cell", tint = NeonCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "CELLULE DE SERVICE (SERVING)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "CONNECTÉE", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }

                Spacer(modifier = Modifier.height(14.dp))

                CellParamRow("Opérateur / Carrier", "${metrics.carrier} (${metrics.mccMnc})")
                CellParamRow("Cell ID (CI / eNodeB)", "${metrics.cellId} (eNB 48225, Sec 0)")
                CellParamRow("Physical Cell ID (PCI)", "${metrics.pci}")
                CellParamRow("Tracking Area Code (TAC)", metrics.tac)
                CellParamRow("EARFCN Fréquence", "${metrics.earfcn} (${metrics.frequencyMhz} MHz)")
                CellParamRow("Bande passante LTE", "15 MHz (75 RBs)")
                CellParamRow("RSRP / RSRQ / SINR", "${metrics.signalDbm} dBm / ${metrics.rsrqDb} dB / ${metrics.sinrDb} dB")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Neighboring Cells Section
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Hub, contentDescription = "Neighbors", tint = NeonBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "CELLULES VOISINES (NEIGHBORS)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "${neighborCells.size} détectées", color = TextSecondary, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                neighborCells.forEach { cell ->
                    NeighborCellItem(cell = cell)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CellParamRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
        Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun NeighborCellItem(cell: NeighborCell) {
    val progress = ((cell.rsrpDbm - (-140f)) / (-44f - (-140f))).coerceIn(0.05f, 1f)
    val color = if (cell.rsrpDbm > -90) NeonCyan else if (cell.rsrpDbm > -105) NeonAmber else NeonRed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceSubtle)
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusDot(color = color, size = 6.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "PCI ${cell.pci} • ${cell.band}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "${cell.rsrpDbm} dBm",
                    color = color,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = color,
                trackColor = Color.White.copy(alpha = 0.08f)
            )
        }
    }
}
