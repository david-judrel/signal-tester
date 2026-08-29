package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiagnosticItem
import com.example.model.NetworkDiagnosticReport
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
fun DiagnosticCard(
    report: NetworkDiagnosticReport = NetworkDiagnosticReport(),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1E293B).copy(alpha = 0.40f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .padding(20.dp)
            .testTag("diagnostic_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header: "Network Diagnostic" + 3 status dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Network Diagnostic",
                    color = Color(0xFFE2E8F0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // 3 status indicator dots: Red, Amber, Cyan (20% opacity)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonRed, androidx.compose.foundation.shape.CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonAmber, androidx.compose.foundation.shape.CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonCyan.copy(alpha = 0.20f), androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Audit breakdown
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiagnosticRowItem(item = report.puissance, tag = "diagnostic_item_puissance")
                HorizontalDivider(color = Color.White.copy(alpha = 0.04f), thickness = 1.dp)
                DiagnosticRowItem(item = report.qualite, tag = "diagnostic_item_qualite")
                HorizontalDivider(color = Color.White.copy(alpha = 0.04f), thickness = 1.dp)
                DiagnosticRowItem(item = report.bruit, tag = "diagnostic_item_bruit")
                HorizontalDivider(color = Color.White.copy(alpha = 0.04f), thickness = 1.dp)
                DiagnosticRowItem(item = report.debit, tag = "diagnostic_item_debit")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Conclusion note: bg-[#EF4444]/10 border-l-2 border-[#EF4444] p-2 rounded-r-lg italic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(NeonRed.copy(alpha = 0.10f))
                    .drawBehind {
                        drawLine(
                            color = NeonRed,
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, size.height),
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("conclusion_box")
            ) {
                Text(
                    text = report.conclusion,
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tip note: text-[11px] text-[#F59E0B] flex items-start gap-2 bg-[#F59E0B]/5 p-2 rounded-lg
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonAmber.copy(alpha = 0.05f))
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .testTag("tip_box")
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "ⓘ",
                        color = NeonAmber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = report.conseil,
                        color = NeonAmber,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun DiagnosticRowItem(
    item: DiagnosticItem,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            StatusDot(color = item.color, size = 9.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.title,
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${item.statusDot} ${item.statusLabel}",
                color = item.color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(${item.metricValue})",
                color = TextTertiary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
