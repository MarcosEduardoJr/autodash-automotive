package com.autodash.core.model

/** Zonas de assento do HVAC (mapeadas p/ VehicleAreaSeat na camada de dados). */
enum class Seat { DRIVER, PASSENGER }

/** Estado do climatizador por zona. Temperaturas em °C (unidade da Car API). */
data class Climate(
    val driverC: Float = 21f,
    val passengerC: Float = 21f,
    val powerOn: Boolean = true,
) {
    fun temp(seat: Seat): Float = if (seat == Seat.DRIVER) driverC else passengerC
}
