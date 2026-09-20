package com.autodash.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.model.Energy

/**
 * DASHBOARD (parked-optimized). Lê tokens do tema (RRO-friendly, docs/06),
 * nunca cores fixas. Em produção usaria collectAsStateWithLifecycle(vm.ui).
 */
@Composable
fun DashboardScreen(state: DashboardUi = DashboardUi()) {
    AutoDashTheme {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            Text("Velocidade: ${state.speed.kmh} km/h")
            Text("Marcha: ${state.gear.label}")
            Text(
                when (val e = state.energy) {
                    is Energy.Battery -> "Bateria: ${e.percent}%"
                    is Energy.Fuel -> "Combustível: ${e.liters} L"
                    Energy.Unavailable -> "Energia: indisponível"
                }
            )
        }
    }
}
