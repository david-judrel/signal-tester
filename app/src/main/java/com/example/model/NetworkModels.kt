package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed

enum class SignalQualityLevel(val labelFr: String, val color: Color, val iconColorTag: String) {
    EXCELLENT("EXCELLENT", NeonCyan, "🟢"),
    BON("BON", NeonCyan, "🟢"),
    MOYEN("MOYEN", NeonAmber, "🟠"),
    FAIBLE("FAIBLE", NeonRed, "🔴"),
    CRITIQUE("CRITIQUE", NeonRed, "🔴")
}

data class SignalMetrics(
    val carrier: String = "MTN CONGO",
    val networkType: String = "4G LTE",
    val signalDbm: Int = -102,
    val status: SignalQualityLevel = SignalQualityLevel.FAIBLE,
    val rsrqDb: Int = -14,
    val sinrDb: Int = 6,
    val band: String = "B3",
    val cellId: String = "12345678",
    val rsrpDb: Int = -102,
    val rssiDb: Int = -78,
    val pci: Int = 246,
    val earfcn: Int = 1650,
    val frequencyMhz: Double = 1800.0,
    val tac: String = "43120",
    val mccMnc: String = "629-10",
    val operatorCountry: String = "Congo (Brazzaville)"
)

data class DiagnosticItem(
    val title: String,
    val statusDot: String, // 🔴, 🟠, 🟢
    val statusLabel: String,
    val metricValue: String,
    val color: Color,
    val tipDetail: String
)

data class NetworkDiagnosticReport(
    val puissance: DiagnosticItem = DiagnosticItem(
        title = "Puissance",
        statusDot = "🔴",
        statusLabel = "Faible",
        metricValue = "-102 dBm (RSRP)",
        color = NeonRed,
        tipDetail = "Niveau de signal inférieur au seuil recommandé (-90 dBm)"
    ),
    val qualite: DiagnosticItem = DiagnosticItem(
        title = "Qualité",
        statusDot = "🟠",
        statusLabel = "Moyenne",
        metricValue = "-14 dB (RSRQ)",
        color = NeonAmber,
        tipDetail = "Qualité de référence modérée causant des retransmissions"
    ),
    val bruit: DiagnosticItem = DiagnosticItem(
        title = "Bruit",
        statusDot = "🔴",
        statusLabel = "Élevé",
        metricValue = "6 dB (SINR)",
        color = NeonRed,
        tipDetail = "Interférences radio notables sur la bande B3"
    ),
    val debit: DiagnosticItem = DiagnosticItem(
        title = "Débit",
        statusDot = "🔴",
        statusLabel = "Faible",
        metricValue = "3.2 Mbps estimé",
        color = NeonRed,
        tipDetail = "Débit bridé par la dégradation du canal RF"
    ),
    val conclusion: String = "CONCLUSION : La puissance du signal est faible. La qualité radio est également dégradée.",
    val conseil: String = "Conseil : Essayez une zone extérieure ou proche d'une fenêtre."
)

data class NeighborCell(
    val cellId: String,
    val pci: Int,
    val band: String,
    val rsrpDbm: Int,
    val rsrqDb: Int,
    val distanceM: Int,
    val isConnected: Boolean = false
)

data class SpeedTestResult(
    val downloadMbps: Float = 0f,
    val uploadMbps: Float = 0f,
    val pingMs: Int = 0,
    val jitterMs: Int = 0,
    val lossPercent: Float = 0f,
    val isTesting: Boolean = false,
    val testPhase: SpeedTestPhase = SpeedTestPhase.IDLE,
    val progress: Float = 0f
)

enum class SpeedTestPhase {
    IDLE, PING, DOWNLOAD, UPLOAD, FINISHED
}

data class HistoryItem(
    val id: String,
    val timestamp: String,
    val carrier: String,
    val networkType: String,
    val signalDbm: Int,
    val status: SignalQualityLevel,
    val downloadSpeed: String,
    val location: String,
    val cellId: String
)

data class SurveyPoint(
    val id: String,
    val xRatio: Float,
    val yRatio: Float,
    val dbm: Int,
    val networkType: String,
    val note: String,
    val timestamp: String
)
