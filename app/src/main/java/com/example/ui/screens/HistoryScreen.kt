package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HistoryItem
import com.example.model.SignalQualityLevel
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
fun HistoryScreen(
    modifier: Modifier = Modifier
) {
    val sampleHistory = remember {
        listOf(
            HistoryItem("1", "Aujourd'hui, 14:28", "MTN CONGO", "4G LTE", -102, SignalQualityLevel.FAIBLE, "3.2 Mbps", "Brazzaville, Bureau", "12345678"),
            HistoryItem("2", "Aujourd'hui, 12:15", "MTN CONGO", "4G LTE", -84, SignalQualityLevel.BON, "28.4 Mbps", "Brazzaville, Fenêtre", "12345678"),
            HistoryItem("3", "Hier, 18:40", "MTN CONGO", "4G LTE", -76, SignalQualityLevel.EXCELLENT, "42.1 Mbps", "Brazzaville, Extérieur", "12345680"),
            HistoryItem("4", "Hier, 09:20", "MTN CONGO", "4G LTE", -108, SignalQualityLevel.CRITIQUE, "1.1 Mbps", "Brazzaville, Sous-sol", "12345672"),
            HistoryItem("5", "27 Août, 16:50", "MTN CONGO", "4G LTE", -94, SignalQualityLevel.MOYEN, "12.8 Mbps", "Brazzaville, Rue", "12345678")
        )
    }

    var selectedFilter by remember { mutableStateOf("TOUS") }

    val filteredList = when (selectedFilter) {
        "FAIBLE" -> sampleHistory.filter { it.status == SignalQualityLevel.FAIBLE || it.status == SignalQualityLevel.CRITIQUE }
        "BON" -> sampleHistory.filter { it.status == SignalQualityLevel.BON || it.status == SignalQualityLevel.EXCELLENT }
        else -> sampleHistory
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(bottom = 85.dp)
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
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "History",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "HISTORIQUE RF",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${filteredList.size} tests enregistrés",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(36.dp)
                    .background(DarkSurfaceElevated, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Clear",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("TOUS", "FAIBLE", "BON").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else DarkSurfaceElevated)
                        .border(
                            1.dp,
                            if (isSelected) NeonCyan else Color.White.copy(alpha = 0.1f),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) NeonCyan else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList, key = { it.id }) { item ->
                HistoryItemCard(item = item)
            }
        }
    }
}

@Composable
private fun HistoryItemCard(item: HistoryItem) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        borderColor = item.status.color.copy(alpha = 0.3f),
        backgroundColor = DarkSurfaceSubtle.copy(alpha = 0.85f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusDot(color = item.status.color, size = 10.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = item.location,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${item.timestamp} • ${item.carrier}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.signalDbm} dBm",
                    color = item.status.color,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = item.downloadSpeed,
                    color = TextCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
