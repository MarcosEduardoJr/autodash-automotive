package com.autodash.data.car

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
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
}
