package com.autodash.data.car

import com.autodash.core.model.Energy
import com.autodash.core.model.Gear

/** Traduz valores crus do VHAL para modelos de domínio. */
object VehicleProps {
    // Constantes de GEAR do VHAL (android.car.VehicleGear).
    private const val GEAR_PARK = 0x0004
    private const val GEAR_REVERSE = 0x0002
    private const val GEAR_NEUTRAL = 0x0001
    private const val GEAR_DRIVE = 0x0008

    fun toGear(raw: Int?): Gear = when (raw) {
        GEAR_PARK -> Gear.PARK
        GEAR_REVERSE -> Gear.REVERSE
        GEAR_NEUTRAL -> Gear.NEUTRAL
        GEAR_DRIVE -> Gear.DRIVE
        else -> Gear.UNKNOWN
    }

    fun toBattery(percent: Float?): Energy =
        percent?.let { Energy.Battery(it.toInt()) } ?: Energy.Unavailable
}
