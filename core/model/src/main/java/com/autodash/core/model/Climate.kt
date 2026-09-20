package com.autodash.core.model

/** Zonas de assento do HVAC (mapeadas p/ VehicleAreaSeat na camada de dados). */
enum class Seat { DRIVER, PASSENGER }

/** Estado do climatizador. Temperaturas em °C; fan 0..6 (unidades da Car API). */
data class Climate(
    val driverC: Float = 21f,
    val passengerC: Float = 21f,
    val powerOn: Boolean = true,
    val acOn: Boolean = true,
    val fanSpeed: Int = 3,
) {
    fun temp(seat: Seat): Float = if (seat == Seat.DRIVER) driverC else passengerC
}
