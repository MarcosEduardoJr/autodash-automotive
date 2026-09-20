package com.autodash.data.car

import android.car.Car
import android.car.VehicleAreaSeat
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import com.autodash.core.model.Climate
import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.Seat
import com.autodash.core.model.VehicleSpeed
import com.autodash.domain.CarRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

/**
 * Implementa o CarRepository do domínio usando a Car API.
 * Único ponto do app que conhece android.car.* (ver data/car/README + docs/02).
 */
class CarPropertyRepository(private val car: Car) : CarRepository {

    private val props =
        car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

    override fun vehicleSpeed(): Flow<VehicleSpeed> = callbackFlow {
        val cb = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                // hardware heterogêneo: só use se estiver disponível
                if (value.status == CarPropertyValue.STATUS_AVAILABLE) {
                    trySend(VehicleSpeed(value.value as Float)) // VHAL entrega m/s
                }
            }
            override fun onErrorEvent(propId: Int, areaId: Int) {
                // propriedade indisponível/erro → degrade a UI, não crashe
            }
        }
        // PERF_VEHICLE_SPEED é CONTINUOUS no VHAL → sample rate
        props.registerCallback(
            cb, VehiclePropertyIds.PERF_VEHICLE_SPEED,
            CarPropertyManager.SENSOR_RATE_NORMAL,
        )
        // SEM isto = leak (carro fica ligado horas). Regra de ouro automotiva.
        awaitClose { props.unregisterCallback(cb) }
    }.conflate() // a UI só precisa do valor mais recente

    override fun gear(): Flow<Gear> = callbackFlow {
        val cb = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                trySend(VehicleProps.toGear(value.value as? Int)) // GEAR_SELECTION é ON_CHANGE
            }
            override fun onErrorEvent(propId: Int, areaId: Int) {}
        }
        props.registerCallback(
            cb, VehiclePropertyIds.GEAR_SELECTION,
            CarPropertyManager.SENSOR_RATE_ONCHANGE,
        )
        awaitClose { props.unregisterCallback(cb) }
    }

    override fun energy(): Flow<Energy> = callbackFlow {
        // Simplificado: EVs expõem EV_BATTERY_LEVEL; combustão, FUEL_LEVEL.
        // Nem todo carro tem os dois — o repositório escolhe o disponível.
        val cb = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                trySend(VehicleProps.toBattery(value.value as? Float))
            }
            override fun onErrorEvent(propId: Int, areaId: Int) {
                trySend(Energy.Unavailable)
            }
        }
        props.registerCallback(
            cb, VehiclePropertyIds.EV_BATTERY_LEVEL,
            CarPropertyManager.SENSOR_RATE_NORMAL,
        )
        awaitClose { props.unregisterCallback(cb) }
    }


    override fun rangeKm(): Flow<Int> = callbackFlow {
        val cb = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                // RANGE_REMAINING vem em METROS no VHAL -> km
                trySend(((value.value as Float) / 1000f).toInt())
            }
            override fun onErrorEvent(propId: Int, areaId: Int) {}
        }
        props.registerCallback(cb, VehiclePropertyIds.RANGE_REMAINING, CarPropertyManager.SENSOR_RATE_NORMAL)
        awaitClose { props.unregisterCallback(cb) }
    }.conflate()

    override fun outsideTempC(): Flow<Int> = callbackFlow {
        val cb = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) { trySend((value.value as Float).toInt()) }
            override fun onErrorEvent(propId: Int, areaId: Int) {}
        }
        props.registerCallback(cb, VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE, CarPropertyManager.SENSOR_RATE_NORMAL)
        awaitClose { props.unregisterCallback(cb) }
    }.conflate()


    // HVAC por ZONA: HVAC_TEMPERATURE_SET é uma propriedade por assento (area).
    private fun seatArea(seat: Seat) =
        if (seat == Seat.DRIVER) VehicleAreaSeat.SEAT_ROW_1_LEFT else VehicleAreaSeat.SEAT_ROW_1_RIGHT

    override fun climate(): Flow<Climate> = callbackFlow {
        var state = Climate()
        val left = VehicleAreaSeat.SEAT_ROW_1_LEFT
        val cb = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                state = when (value.propertyId) {
                    VehiclePropertyIds.HVAC_TEMPERATURE_SET -> {
                        val t = value.value as Float
                        if (value.areaId == left) state.copy(driverC = t) else state.copy(passengerC = t)
                    }
                    VehiclePropertyIds.HVAC_POWER_ON -> state.copy(powerOn = value.value as Boolean)
                    VehiclePropertyIds.HVAC_AC_ON -> state.copy(acOn = value.value as Boolean)
                    VehiclePropertyIds.HVAC_FAN_SPEED -> state.copy(fanSpeed = value.value as Int)
                    else -> state
                }
                trySend(state)
            }
            override fun onErrorEvent(propId: Int, areaId: Int) {}
        }
        val rate = CarPropertyManager.SENSOR_RATE_ONCHANGE
        props.registerCallback(cb, VehiclePropertyIds.HVAC_TEMPERATURE_SET, rate)
        props.registerCallback(cb, VehiclePropertyIds.HVAC_POWER_ON, rate)
        props.registerCallback(cb, VehiclePropertyIds.HVAC_AC_ON, rate)
        props.registerCallback(cb, VehiclePropertyIds.HVAC_FAN_SPEED, rate)
        trySend(state)
        awaitClose { props.unregisterCallback(cb) }
    }.conflate()

    // ESCRITA no veículo: exige CONTROL_CAR_CLIMATE (signature|privileged) — ver docs/04.
    override suspend fun setSeatTemp(seat: Seat, tempC: Float) {
        runCatching { props.setFloatProperty(VehiclePropertyIds.HVAC_TEMPERATURE_SET, seatArea(seat), tempC.coerceIn(16f, 28f)) }
    }
    override suspend fun setPower(on: Boolean) {
        runCatching { props.setBooleanProperty(VehiclePropertyIds.HVAC_POWER_ON, 0, on) }
    }
    override suspend fun setAc(on: Boolean) {
        runCatching { props.setBooleanProperty(VehiclePropertyIds.HVAC_AC_ON, 0, on) }
    }
    override suspend fun setFan(speed: Int) {
        runCatching { props.setIntProperty(VehiclePropertyIds.HVAC_FAN_SPEED, 0, speed.coerceIn(0, 6)) }
    }
}
