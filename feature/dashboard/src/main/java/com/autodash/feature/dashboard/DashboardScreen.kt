package com.autodash.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.model.Energy

/**
 * DASHBOARD (parked-optimized). Lê tokens do tema (RRO-friendly, docs/06), nunca cores fixas.
 * Estado vem do ViewModel via CarRepository; aqui é stateless (recebe DashboardUi).
 */
@Composable
fun DashboardScreen(state: DashboardUi = DashboardUi()) {
    AutoDashTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.fillMaxSize().padding(56.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    "AUTODASH · CLUSTER",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold, letterSpacing = 4.sp, fontSize = 16.sp,
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        "${state.speed.kmh}",
                        fontSize = 180.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        "  km/h", fontSize = 34.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 34.dp),
                    )
                }
                Spacer(Modifier.height(28.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Tile("MARCHA", state.gear.label)
                    Tile(
                        "ENERGIA",
                        when (val e = state.energy) {
                            is Energy.Battery -> "${e.percent}%"
                            is Energy.Fuel -> "${e.liters} L"
                            Energy.Unavailable -> "—"
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun Tile(label: String, value: String) {
    Column(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 28.dp, vertical = 18.dp),
    ) {
        Text(label, fontSize = 13.sp, letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 40.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface)
    }
}
