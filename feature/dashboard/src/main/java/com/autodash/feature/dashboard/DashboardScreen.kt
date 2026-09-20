package com.autodash.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autodash.core.designsystem.LocalBrandTokens
import com.autodash.core.model.Energy

/** Decisão de layout, PURA → testável sem device. Wide = tela larga (landscape). */
fun isWide(widthDp: Int, heightDp: Int): Boolean = widthDp >= heightDp

private fun energyLabel(e: Energy): String = when (e) {
    is Energy.Battery -> "${e.percent}%"
    is Energy.Fuel -> "${e.liters} L"
    Energy.Unavailable -> "—"
}

/**
 * Dashboard responsivo. NÃO aplica tema (o host aplica AutoDashTheme com a marca).
 * BoxWithConstraints decide portrait × landscape por medida real → adapta em qualquer head unit.
 */
@Composable
fun DashboardScreen(state: DashboardUi = DashboardUi(), onCycleBrand: () -> Unit = {}) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(28.dp)) {
            val wide = isWide(maxWidth.value.toInt(), maxHeight.value.toInt())
            Column(Modifier.fillMaxSize()) {
                BrandBar(onCycleBrand)
                if (wide) {
                    Row(
                        Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        Box(Modifier.weight(1.5f)) { Speed(state, big = true) }
                        Column(
                            Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Tile("MARCHA", state.gear.label)
                            Tile("ENERGIA", energyLabel(state.energy))
                        }
                    }
                } else {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Speed(state, big = false)
                        Spacer(Modifier.height(28.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Tile("MARCHA", state.gear.label)
                            Tile("ENERGIA", energyLabel(state.energy))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandBar(onCycleBrand: () -> Unit) {
    val brand = LocalBrandTokens.current
    Row(
        Modifier.fillMaxWidth().padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(34.dp).clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                brand.name.take(1),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold, fontSize = 18.sp,
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                "AUTODASH", color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold, letterSpacing = 3.sp, fontSize = 14.sp,
            )
            Text(
                brand.name, color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 2.sp, fontSize = 11.sp,
            )
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onCycleBrand) { Text("Trocar marca") }
    }
}

@Composable
private fun Speed(state: DashboardUi, big: Boolean) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            "${state.speed.kmh}",
            fontSize = (if (big) 190 else 128).sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            "  km/h", fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = if (big) 34.dp else 24.dp),
        )
    }
}

@Composable
private fun Tile(label: String, value: String) {
    Column(
        Modifier.clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 26.dp, vertical = 16.dp),
    ) {
        Text(
            label, fontSize = 12.sp, letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            value, fontSize = 38.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
