package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.CustomBottomNav
import com.example.ui.components.FuturisticBackground
import com.example.ui.components.NavDestination
import com.example.ui.screens.CellInfoScreen
import com.example.ui.screens.CoverageMapScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MainSignalDashboardScreen
import com.example.ui.screens.SpeedTestScreen
import com.example.ui.screens.SurveyModeScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SignalTestApp()
            }
        }
    }
}

@Composable
fun SignalTestApp() {
    var currentDestination by remember { mutableStateOf(NavDestination.SIGNAL) }

    FuturisticBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                CustomBottomNav(
                    selectedDestination = currentDestination,
                    onDestinationSelected = { destination ->
                        currentDestination = destination
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { _ ->
            Box(modifier = Modifier.fillMaxSize()) {
                Crossfade(
                    targetState = currentDestination,
                    animationSpec = tween(250),
                    label = "ScreenTransition"
                ) { destination ->
                    when (destination) {
                        NavDestination.SIGNAL -> MainSignalDashboardScreen(
                            onNavigateToSpeedTest = {
                                currentDestination = NavDestination.SPEED_TEST
                            }
                        )
                        NavDestination.SPEED_TEST -> SpeedTestScreen()
                        NavDestination.COVERAGE_MAP -> CoverageMapScreen()
                        NavDestination.HISTORY -> HistoryScreen()
                        NavDestination.CELL_INFO -> CellInfoScreen()
                        NavDestination.SURVEY_MODE -> SurveyModeScreen()
                    }
                }
            }
        }
    }
}

