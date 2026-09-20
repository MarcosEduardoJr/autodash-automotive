package com.autodash.domain

import com.autodash.core.model.VehicleSpeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Use case: observar a velocidade. Puro — não sabe que existe CarPropertyManager.
 * distinctUntilChanged porque PERF_VEHICLE_SPEED (CONTINUOUS) emite muito; a UI
 * só precisa reagir a mudanças (backpressure fica no repositório — ver data/car).
 */
class ObserveVehicleSpeed(private val car: CarRepository) {
    operator fun invoke(): Flow<VehicleSpeed> = car.vehicleSpeed().distinctUntilChanged()
}
