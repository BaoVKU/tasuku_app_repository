package com.example.tasuku.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasuku.ui.components.layout.BottomNavigationBar
import com.example.tasuku.ui.components.layout.NavTopBar
import com.example.tasuku.ui.components.schedule.Event
import com.example.tasuku.ui.components.schedule.Schedule
import com.example.tasuku.ui.components.schedule.sampleEvents
import com.example.tasuku.ui.navigation.NavigationDestination
import com.example.tasuku.ui.navigation.ScheduleDestination
import com.example.tasuku.ui.viewmodels.AppViewModelProvider
import com.example.tasuku.ui.viewmodels.ScheduleUiState
import com.example.tasuku.ui.viewmodels.ScheduleViewModel
import java.time.LocalDateTime
import kotlin.random.Random

val colorStringList = listOf("#64748b", "#6b7280", "#71717a", "#737373", "#78716c", "#ef4444", "#f97316", "#f59e0b",
    "#eab308", "#84cc16", "#22c55e", "#10b981", "#14b8a6", "#06b6d4", "#0ea5e9", "#3b82f6", "#6366f1",
    "#8b5cf6", "#a855f7", "#d946ef", "#ec4899", "#f43f5e", "#475569", "#4b5563", "#52525b", "#525252",
    "#57534e", "#dc2626", "#ea580c", "#d97706", "#ca8a04", "#65a30d", "#16a34a", "#059669", "#0d9488",
    "#0891b2", "#0284c7", "#2563eb", "#4f46e5", "#7c3aed", "#9333ea", "#c026d3", "#db2777", "#e11d48",
    "#334155", "#374151", "#3f3f46", "#404040", "#44403c", "#b91c1c", "#c2410c", "#b45309", "#a16207",
    "#4d7c0f", "#15803d", "#047857", "#0f766e", "#0e7490", "#0369a1", "#1d4ed8", "#4338ca", "#6d28d9",
    "#7e22ce", "#a21caf", "#be185d", "#be123c", "#94a3b8", "#9ca3af", "#a1a1aa", "#a3a3a3", "#a8a29e",
    "#f87171", "#fb923c", "#fbbf24", "#facc15", "#a3e635", "#4ade80", "#34d399", "#2dd4bf", "#22d3ee",
    "#38bdf8", "#60a5fa", "#818cf8", "#a78bfa", "#c084fc", "#e879f9", "#f472b6", "#fb7185")

val colorList = colorStringList.map { Color(android.graphics.Color.parseColor(it)) }

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigate: (NavigationDestination) -> Unit = {}
) {
    val scheduleUiState by viewModel.scheduleUiState.collectAsState()

    Scaffold(
        topBar = {
            NavTopBar(title = stringResource(id = ScheduleDestination.titleRes))


        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = ScheduleDestination,
                onNavClick = onNavigate
            )

        }, modifier = modifier
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (scheduleUiState) {
                is ScheduleUiState.Loading -> {
                    Text(text = "Loading...")
                }

                is ScheduleUiState.Error -> {
                    Text(text = "Error: ${(scheduleUiState as ScheduleUiState.Error).message}")
                }

                is ScheduleUiState.Success -> {
                    val receivedEvents = (scheduleUiState as ScheduleUiState.Success).data
                    val events = receivedEvents.map { event ->
                        Event(
                            name = event.title,
                            color = colorList[Random.nextInt(colorList.size)],
                            start = LocalDateTime.parse(event.start.replace(" ", "T")),
                            end = LocalDateTime.parse(event.end.replace(" ", "T"))
                        )
                    }
                    Schedule(events = events)
                }
            }
        }
    }
}