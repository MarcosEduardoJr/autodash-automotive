package com.autodash.feature.climate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autodash.core.designsystem.ChakraPetch
import com.autodash.core.model.Climate
import com.autodash.core.model.Seat

/**
 * CLIMA — HVAC por ZONA + POWER/A-C/FAN. Padrões de UX: componentes Material3 (Switch,
 * FilledIconButton) com ripple, alvo grande (≥64dp p/ carro) e contentDescription (TalkBack).
 * Desligar POWER **desabilita e apaga** zonas/A-C/FAN — feedback claro do estado.
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
    val on = state.powerOn
    Surface(Modifier.fillMaxSize(), color = cs.background) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(28.dp)) {
            val wide = maxWidth >= maxHeight
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                val zones = @Composable { m: Modifier ->
                    Row(m, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        ZoneCard(stringResource(R.string.driver), state.temp(Seat.DRIVER), on, Modifier.weight(1f).fillMaxHeight()) { onDelta(Seat.DRIVER, it) }
                        ZoneCard(stringResource(R.string.passenger), state.temp(Seat.PASSENGER), on, Modifier.weight(1f).fillMaxHeight()) { onDelta(Seat.PASSENGER, it) }
                    }
                }
                if (wide) {
                    zones(Modifier.fillMaxWidth().weight(1f))
                } else {
                    Column(Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ZoneCard(stringResource(R.string.driver), state.temp(Seat.DRIVER), on, Modifier.fillMaxWidth().weight(1f)) { onDelta(Seat.DRIVER, it) }
                        ZoneCard(stringResource(R.string.passenger), state.temp(Seat.PASSENGER), on, Modifier.fillMaxWidth().weight(1f)) { onDelta(Seat.PASSENGER, it) }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SwitchTile("POWER", Icons.Filled.PowerSettingsNew, on, enabled = true, Modifier.weight(1f)) { onPower() }
                    SwitchTile("A/C", Icons.Filled.AcUnit, state.acOn && on, enabled = on, Modifier.weight(1f)) { onAc() }
                    FanTile(state.fanSpeed, enabled = on, Modifier.weight(1.3f), onFan)
                }
            }
        }
    }
}

@Composable
private fun ZoneCard(label: String, tempC: Float, enabled: Boolean, modifier: Modifier, onDelta: (Float) -> Unit) {
    val cs = MaterialTheme.colorScheme
    val a = if (enabled) 1f else 0.35f
    Column(
        modifier.clip(RoundedCornerShape(24.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(24.dp)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(label.uppercase(), color = cs.onSurfaceVariant.copy(alpha = a), fontFamily = ChakraPetch,
            letterSpacing = 3.sp, fontSize = 14.sp)
        Spacer(Modifier.height(14.dp))
        Text("%.1f°".format(tempC), color = cs.onSurface.copy(alpha = a), fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, fontSize = 72.sp)
        Spacer(Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            BigIconButton(Icons.Filled.Remove, "Diminuir temperatura de $label", enabled) { onDelta(-0.5f) }
            BigIconButton(Icons.Filled.Add, "Aumentar temperatura de $label", enabled) { onDelta(0.5f) }
        }
    }
}

@Composable
private fun SwitchTile(label: String, icon: ImageVector, checked: Boolean, enabled: Boolean, modifier: Modifier, onToggle: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val a = if (enabled) 1f else 0.4f
    Row(
        modifier.clip(RoundedCornerShape(18.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(18.dp)).padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null,
            tint = (if (checked) cs.primary else cs.onSurfaceVariant).copy(alpha = a))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, color = cs.onSurface.copy(alpha = a), fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 15.sp)
            Text(if (checked) stringResource(R.string.on) else stringResource(R.string.off),
                color = cs.onSurfaceVariant.copy(alpha = a), fontFamily = ChakraPetch, fontSize = 11.sp)
        }
        Spacer(Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { onToggle() }, enabled = enabled)
    }
}

@Composable
private fun FanTile(fan: Int, enabled: Boolean, modifier: Modifier, onFan: (Int) -> Unit) {
    val cs = MaterialTheme.colorScheme
    val a = if (enabled) 1f else 0.4f
    Row(
        modifier.clip(RoundedCornerShape(18.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(18.dp)).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Air, contentDescription = null, tint = cs.onSurfaceVariant.copy(alpha = a))
        Spacer(Modifier.width(10.dp))
        Text("FAN", color = cs.onSurface.copy(alpha = a), fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 14.sp)
        Spacer(Modifier.weight(1f))
        BigIconButton(Icons.Filled.Remove, "Reduzir ventilação", enabled, 52.dp) { onFan(-1) }
        Text("$fan", color = cs.onSurface.copy(alpha = a), fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, fontSize = 26.sp,
            modifier = Modifier.padding(horizontal = 14.dp))
        BigIconButton(Icons.Filled.Add, "Aumentar ventilação", enabled, 52.dp) { onFan(1) }
    }
}

@Composable
private fun BigIconButton(icon: ImageVector, desc: String, enabled: Boolean, size: androidx.compose.ui.unit.Dp = 64.dp, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    FilledIconButton(
        onClick = onClick, enabled = enabled, modifier = Modifier.size(size),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = cs.primary, contentColor = cs.onPrimary,
            disabledContainerColor = cs.surfaceVariant, disabledContentColor = cs.onSurfaceVariant,
        ),
    ) {
        Icon(icon, contentDescription = desc, modifier = Modifier.size(size * 0.42f))
    }
}
