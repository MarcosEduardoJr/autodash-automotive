package com.autodash.core.model

/** Velocidade do veículo já em unidade de domínio. VHAL entrega em m/s. */
@JvmInline
value class VehicleSpeed(val metersPerSecond: Float) {
    val kmh: Int get() = (metersPerSecond * 3.6f).toInt()
    val mph: Int get() = (metersPerSecond * 2.2369f).toInt()
}

/** Marcha selecionada (traduzida de GEAR_SELECTION do VHAL). sealed = when exaustivo. */
enum class Gear(val label: String) {
    PARK("P"), REVERSE("R"), NEUTRAL("N"), DRIVE("D"), UNKNOWN("—")
}

/** Energia: EV (bateria %) ou combustível (litros). Nem todo carro tem os dois. */
sealed interface Energy {
    data class Battery(val percent: Int) : Energy
    data class Fuel(val liters: Float) : Energy
    data object Unavailable : Energy   // hardware heterogêneo — degrade, não crashe
}
