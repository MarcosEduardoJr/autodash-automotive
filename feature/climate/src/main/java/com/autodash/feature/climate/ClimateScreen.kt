package com.autodash.feature.climate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autodash.core.designsystem.ChakraPetch
import com.autodash.core.model.Climate
import com.autodash.core.model.Seat

/**
 * CLIMA — HVAC completo por ZONA: temperatura de cada assento (HVAC_TEMPERATURE_SET por area),
 * + POWER / A/C / FAN. Todo +/- e toggle chama setProperty na camada de dados (docs/02, 04).
 * Sem tema próprio (host aplica AutoDashTheme). Responsivo por medida real.
 */
@Composable
fun ClimateScreen(
    state: Climate = Climate(),
    onDelta: (Seat, Float) -> Unit = { _, _ -> },
    onPower: () -> Unit = {},
    onAc: () -> Unit = {},
    onFan: (Int) -> Unit = {},
) {
    val cs = MaterialTheme.colorScheme
    Surface(Modifier.fillMaxSize(), color = cs.background) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(28.dp)) {
            val wide = maxWidth >= maxHeight
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                val zones: @Composable (Modifier) -> Unit = { m ->
                    ZoneCard(stringResource(R.string.driver), state.temp(Seat.DRIVER), state.powerOn, m) { onDelta(Seat.DRIVER, it) }
                }
                if (wide) {
                    Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        ZoneCard(stringResource(R.string.driver), state.temp(Seat.DRIVER), state.powerOn, Modifier.weight(1f).fillMaxHeight()) { onDelta(Seat.DRIVER, it) }
                        ZoneCard(stringResource(R.string.passenger), state.temp(Seat.PASSENGER), state.powerOn, Modifier.weight(1f).fillMaxHeight()) { onDelta(Seat.PASSENGER, it) }
                    }
                } else {
                    Column(Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ZoneCard(stringResource(R.string.driver), state.temp(Seat.DRIVER), state.powerOn, Modifier.fillMaxWidth().weight(1f)) { onDelta(Seat.DRIVER, it) }
                        ZoneCard(stringResource(R.string.passenger), state.temp(Seat.PASSENGER), state.powerOn, Modifier.fillMaxWidth().weight(1f)) { onDelta(Seat.PASSENGER, it) }
                    }
                }
                ControlsBar(state, cs, onPower, onAc, onFan)
            }
        }
    }
}

@Composable
private fun ZoneCard(label: String, tempC: Float, enabled: Boolean, modifier: Modifier, onDelta: (Float) -> Unit) {
    val cs = MaterialTheme.colorScheme
    val alpha = if (enabled) 1f else 0.35f
    Column(
        modifier.clip(RoundedCornerShape(24.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(24.dp)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(label.uppercase(), color = cs.onSurfaceVariant.copy(alpha = alpha), fontFamily = ChakraPetch,
            letterSpacing = 3.sp, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Text("%.1f°".format(tempC), color = cs.onSurface.copy(alpha = alpha), fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, fontSize = 76.sp)
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            RoundBtn("−", 68.dp, cs) { if (enabled) onDelta(-0.5f) }
            RoundBtn("+", 68.dp, cs) { if (enabled) onDelta(0.5f) }
        }
    }
}

@Composable
private fun ControlsBar(state: Climate, cs: ColorScheme, onPower: () -> Unit, onAc: () -> Unit, onFan: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(20.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Toggle("POWER", state.powerOn, cs, Modifier.weight(1f), onPower)
        Toggle("A/C", state.acOn, cs, Modifier.weight(1f), onAc)
        // FAN
        Row(
            Modifier.weight(1.4f).clip(RoundedCornerShape(14.dp)).background(cs.background)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("FAN", color = cs.onSurfaceVariant, fontFamily = ChakraPetch, letterSpacing = 2.sp, fontSize = 13.sp)
            Spacer(Modifier.weight(1f))
            RoundBtn("−", 44.dp, cs) { onFan(-1) }
            Spacer(Modifier.width(12.dp))
            Text("${state.fanSpeed}", color = cs.onSurface, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Spacer(Modifier.width(12.dp))
            RoundBtn("+", 44.dp, cs) { onFan(1) }
        }
    }
}

@Composable
private fun Toggle(label: String, on: Boolean, cs: ColorScheme, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.clip(RoundedCornerShape(14.dp))
            .background(if (on) cs.primary else cs.background)
            .clickable { onClick() }.padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, color = if (on) cs.onPrimary else cs.onSurfaceVariant, fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 16.sp)
    }
}

@Composable
private fun RoundBtn(symbol: String, size: androidx.compose.ui.unit.Dp, cs: ColorScheme, onClick: () -> Unit) {
    Box(
        Modifier.size(size).clip(CircleShape).background(cs.primary).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(symbol, color = cs.onPrimary, fontFamily = ChakraPetch, fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.5f).sp, textAlign = TextAlign.Center)
    }
}
