package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NetworkDiagnosticReport
import com.example.model.SignalMetrics
import com.example.model.SignalQualityLevel
import com.example.ui.components.DiagnosticCard
import com.example.ui.components.GlassCard
import com.example.ui.components.PulsingButton
import com.example.ui.components.SignalGauge
import com.example.ui.components.StatusDot
import com.example.ui.components.TechnicalGrid
import com.example.ui.theme.DarkSurface
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainSignalDashboardScreen(
    onNavigateToSpeedTest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var metrics by remember { mutableStateOf(SignalMetrics()) }
    var isScanning by remember { mutableStateOf(false) }
    var selectedSim by remember { mutableStateOf("SIM 1") }
    var report by remember { mutableStateOf(NetworkDiagnosticReport()) }
    val coroutineScope = rememberCoroutineScope()

    val performRfScan = {
        if (!isScanning) {
            coroutineScope.launch {
                isScanning = true
                // Simulate RF sweep testing
                delay(600)
                metrics = metrics.copy(signalDbm = -105, rsrqDb = -15, sinrDb = 5)
                delay(700)
                metrics = metrics.copy(signalDbm = -98, rsrqDb = -13, sinrDb = 7)
                delay(700)
                // settle back to realistic current sample or updated values
                metrics = metrics.copy(
                    signalDbm = -102,
                    status = SignalQualityLevel.FAIBLE,
                    rsrqDb = -14,
                    sinrDb = 6,
                    band = "B3",
                    cellId = "12345678"
                )
                isScanning = false
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
        // TOP HEADER: Carrier Name ("MTN CONGO") + Sleek Badge ("4G LTE")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // SIM Switcher
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B).copy(alpha = 0.7f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .clickable {
                            selectedSim = if (selectedSim == "SIM 1") "SIM 2" else "SIM 1"
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SimCard,
                            contentDescription = "SIM",
                            tint = NeonCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedSim,
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Centered Carrier Name: text-xl font-bold tracking-tight text-white uppercase opacity-90
                Text(
                    text = metrics.carrier.uppercase(),
                    color = Color.White.copy(alpha = 0.90f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.testTag("carrier_name_text")
                )

                // Refresh button
                IconButton(
                    onClick = { performRfScan() },
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color(0xFF1E293B).copy(alpha = 0.7f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                        .testTag("refresh_rf_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = if (isScanning) NeonCyan else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sleek Badge: inline-flex items-center px-3 py-0.5 rounded-full bg-[#1E293B] border border-[#00FFCC]/20 text-[#00FFCC] text-xs font-bold tracking-widest
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                    .border(1.dp, NeonCyan.copy(alpha = 0.20f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 3.dp)
                    .testTag("network_type_badge"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${metrics.networkType} • B3",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))


        // MAIN SIGNAL GAUGE: Displays -102 dBm + FAIBLE status
        SignalGauge(
            signalDbm = metrics.signalDbm,
            statusLevel = metrics.status,
            isScanning = isScanning,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // TECHNICAL 2x2 GRID (RSRQ: -14 dB, SINR: 6 dB, BAND: B3, CELL ID: 12345678)
        TechnicalGrid(
            metrics = metrics,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ACTION BUTTON: Prominent custom gradient floating / action button labeled "[ TESTER ]" / "LANCER LE TEST"
        PulsingButton(
            text = "[ LANCER LE TEST ]",
            isLoading = isScanning,
            onClick = { performRfScan() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // DIAGNOSTIC SECTION / CARD
        DiagnosticCard(
            report = report,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
