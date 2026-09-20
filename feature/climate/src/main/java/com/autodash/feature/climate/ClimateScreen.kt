package com.autodash.feature.climate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autodash.core.designsystem.ChakraPetch
import com.autodash.core.model.Climate
import com.autodash.core.model.Seat

/**
 * Tela de CLIMA — HVAC por ZONA (motorista/passageiro). Cada zona é uma 'area' da
 * propriedade HVAC_TEMPERATURE_SET; +/- chama setProperty na camada de dados (docs/02, 04).
 * Sem tema próprio (o host aplica AutoDashTheme). Responsivo por medida real.
 */
@Composable
fun ClimateScreen(state: Climate = Climate(), onDelta: (Seat, Float) -> Unit = { _, _ -> }) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(28.dp)) {
            val wide = maxWidth >= maxHeight
            if (wide) {
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    ZoneCard(stringResourceDriver(), state.temp(Seat.DRIVER), Modifier.weight(1f).fillMaxHeight()) { onDelta(Seat.DRIVER, it) }
                    ZoneCard(stringResourcePassenger(), state.temp(Seat.PASSENGER), Modifier.weight(1f).fillMaxHeight()) { onDelta(Seat.PASSENGER, it) }
                }
            } else {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    ZoneCard(stringResourceDriver(), state.temp(Seat.DRIVER), Modifier.fillMaxWidth().weight(1f)) { onDelta(Seat.DRIVER, it) }
                    ZoneCard(stringResourcePassenger(), state.temp(Seat.PASSENGER), Modifier.fillMaxWidth().weight(1f)) { onDelta(Seat.PASSENGER, it) }
                }
            }
        }
    }
}

@Composable private fun stringResourceDriver() =
    androidx.compose.ui.res.stringResource(R.string.driver)
@Composable private fun stringResourcePassenger() =
    androidx.compose.ui.res.stringResource(R.string.passenger)

@Composable
private fun ZoneCard(label: String, tempC: Float, modifier: Modifier, onDelta: (Float) -> Unit) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier.clip(RoundedCornerShape(24.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(24.dp)).padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(label.uppercase(), color = cs.onSurfaceVariant, fontFamily = ChakraPetch,
            letterSpacing = 3.sp, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))
        Text("%.1f°".format(tempC), color = cs.onSurface, fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, fontSize = 84.sp)
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            StepButton("−", cs) { onDelta(-0.5f) }
            StepButton("+", cs) { onDelta(0.5f) }
        }
    }
}

@Composable
private fun StepButton(symbol: String, cs: androidx.compose.material3.ColorScheme, onClick: () -> Unit) {
    Box(
        Modifier.size(72.dp).clip(CircleShape).background(cs.primary).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(symbol, color = cs.onPrimary, fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, fontSize = 40.sp, textAlign = TextAlign.Center)
    }
}
