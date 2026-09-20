package com.autodash.feature.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autodash.core.designsystem.ChakraPetch
import com.autodash.core.designsystem.LocalBrandTokens
import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.UnitSystem
import com.autodash.core.model.speedIn
import kotlin.math.cos
import kotlin.math.sin

/** Decisão de layout, PURA → testável sem device. Wide = tela larga (landscape). */
fun isWide(widthDp: Int, heightDp: Int): Boolean = widthDp >= heightDp

private fun batteryPercent(e: Energy): Int = when (e) {
    is Energy.Battery -> e.percent
    is Energy.Fuel -> ((e.liters / 60f) * 100f).toInt().coerceIn(0, 100)
    Energy.Unavailable -> 0
}

/**
 * Cluster AutoDash. White-label: cor/tipografia vêm do design system (brand tokens); SEM
 * seletor de marca (a marca é fixa por build). i18n: km/h×mph, °C×°F, km×mi por locale.
 * Gauge e anel são desenhados no Canvas → cara de instrumento, não de app genérico.
 * Responsivo por medida real (isWide). NÃO aplica tema (o host aplica AutoDashTheme).
 */
@Composable
fun DashboardScreen(state: DashboardUi = DashboardUi()) {
    val imperial = booleanResource(R.bool.use_imperial)
    val units = if (imperial) UnitSystem.IMPERIAL else UnitSystem.METRIC
    val speedVal = state.speed.speedIn(units)
    val speedMax = if (imperial) 140 else 220
    val rangeVal = if (imperial) (state.rangeKm * 0.621f).toInt() else state.rangeKm
    val rangeUnit = if (imperial) "mi" else "km"
    val tempVal = if (imperial) state.outsideTempC * 9 / 5 + 32 else state.outsideTempC
    val tempUnit = if (imperial) "°F" else "°C"
    val pct = batteryPercent(state.energy)

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 22.dp)) {
            val wide = isWide(maxWidth.value.toInt(), maxHeight.value.toInt())
            Column(Modifier.fillMaxSize()) {
                BrandHeader()
                Spacer(Modifier.height(if (wide) 8.dp else 18.dp))
                if (wide) {
                    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                        SpeedGauge(speedVal, speedMax, units.speedLabel, Modifier.weight(1.4f).fillMaxHeight())
                        Column(
                            Modifier.weight(1f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            GearSelector(state.gear)
                            BatteryRing(pct, rangeVal, rangeUnit, Modifier.weight(1f).fillMaxWidth())
                            InfoRow(tempVal, tempUnit)
                        }
                    }
                } else {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        SpeedGauge(speedVal, speedMax, units.speedLabel, Modifier.fillMaxWidth().weight(1.1f))
                        GearSelector(state.gear)
                        Row(
                            Modifier.fillMaxWidth().weight(0.8f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            BatteryRing(pct, rangeVal, rangeUnit, Modifier.weight(1f).fillMaxHeight())
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                StatCard(stringResource(R.string.gear), state.gear.label)
                                StatCard("TEMP", "$tempVal$tempUnit")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandHeader() {
    val brand = LocalBrandTokens.current
    val cs = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(38.dp).clip(RoundedCornerShape(11.dp)).background(brandGradient(cs.primary)),
            contentAlignment = Alignment.Center,
        ) {
            Text(brand.name.take(1), color = cs.onPrimary, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, fontSize = 19.sp)
        }
        Spacer(Modifier.width(13.dp))
        Column {
            Text("AUTODASH", color = cs.onBackground, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, letterSpacing = 4.sp, fontSize = 15.sp)
            Text(brand.name, color = cs.onSurfaceVariant, fontFamily = ChakraPetch,
                letterSpacing = 3.sp, fontSize = 10.sp)
        }
        Spacer(Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(cs.primary))
            Spacer(Modifier.width(7.dp))
            Text("LIVE", color = cs.primary, fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold, letterSpacing = 3.sp, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SpeedGauge(value: Int, max: Int, unit: String, modifier: Modifier) {
    val cs = MaterialTheme.colorScheme
    val accent = cs.primary
    val track = cs.surfaceVariant
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val start = 135f
            val sweep = 270f
            val stroke = size.minDimension * 0.05f
            val d = size.minDimension - stroke * 2.4f
            val tl = Offset((size.width - d) / 2f, (size.height - d) / 2f)
            val arc = Size(d, d)
            val cx = size.width / 2f
            val cy = size.height / 2f
            val r = d / 2f
            // ticks
            for (i in 0..10) {
                val a = Math.toRadians((start + sweep * i / 10f).toDouble())
                val outer = r + stroke * 0.9f
                val inner = r + stroke * (if (i % 5 == 0) 0.0f else 0.45f)
                drawLine(
                    color = track,
                    start = Offset(cx + (inner) * cos(a).toFloat(), cy + (inner) * sin(a).toFloat()),
                    end = Offset(cx + (outer) * cos(a).toFloat(), cy + (outer) * sin(a).toFloat()),
                    strokeWidth = if (i % 5 == 0) 4f else 2f,
                )
            }
            // track arc
            drawArc(track, start, sweep, false, tl, arc, style = Stroke(stroke, cap = StrokeCap.Round))
            // glow
            val frac = (value.toFloat() / max).coerceIn(0f, 1f)
            drawArc(accent.copy(alpha = 0.18f), start, sweep * frac, false, tl, arc,
                style = Stroke(stroke * 2.4f, cap = StrokeCap.Round))
            // progress arc (gradient)
            drawArc(
                brush = Brush.sweepGradient(
                    0f to accent.copy(alpha = 0.55f), 0.5f to accent, 1f to accent,
                    center = Offset(cx, cy),
                ),
                startAngle = start, sweepAngle = sweep * frac, useCenter = false,
                topLeft = tl, size = arc, style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$value", color = cs.onBackground, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, fontSize = 108.sp)
            Text(unit, color = cs.onSurfaceVariant, fontFamily = ChakraPetch,
                letterSpacing = 5.sp, fontSize = 20.sp)
        }
    }
}

@Composable
private fun BatteryRing(pct: Int, range: Int, rangeUnit: String, modifier: Modifier) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier.clip(RoundedCornerShape(22.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(22.dp)).padding(18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = size.minDimension * 0.09f
            val d = size.minDimension - stroke
            val tl = Offset((size.width - d) / 2f, (size.height - d) / 2f)
            val arc = Size(d, d)
            drawArc(cs.surfaceVariant, -90f, 360f, false, tl, arc, style = Stroke(stroke, cap = StrokeCap.Round))
            drawArc(cs.primary, -90f, 360f * (pct / 100f), false, tl, arc, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$pct%", color = cs.onBackground, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, fontSize = 34.sp)
            Text("$range $rangeUnit", color = cs.onSurfaceVariant, fontFamily = ChakraPetch,
                letterSpacing = 2.sp, fontSize = 14.sp)
        }
    }
}

@Composable
private fun GearSelector(current: Gear) {
    val cs = MaterialTheme.colorScheme
    val gears = listOf(Gear.PARK, Gear.REVERSE, Gear.NEUTRAL, Gear.DRIVE)
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(16.dp)).padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        gears.forEach { g ->
            val on = g == current
            Box(
                Modifier.weight(1f).clip(RoundedCornerShape(11.dp))
                    .background(if (on) cs.primary else Color.Transparent)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(g.label, color = if (on) cs.onPrimary else cs.onSurfaceVariant,
                    fontFamily = ChakraPetch, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }
    }
}

@Composable
private fun InfoRow(temp: Int, tempUnit: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        StatCard("MODO", "COMFORT", Modifier.weight(1f))
        StatCard("TEMP", "$temp$tempUnit", Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(cs.surface)
            .border(1.dp, cs.surfaceVariant, RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Text(label.uppercase(), color = cs.onSurfaceVariant, fontFamily = ChakraPetch,
            letterSpacing = 2.sp, fontSize = 11.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, color = cs.onSurface, fontFamily = ChakraPetch,
            fontWeight = FontWeight.Bold, fontSize = 26.sp)
    }
}

private fun brandGradient(c: Color): Brush =
    Brush.linearGradient(listOf(c, c.copy(alpha = 0.6f)))
