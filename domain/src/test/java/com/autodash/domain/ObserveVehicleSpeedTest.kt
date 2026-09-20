package com.autodash.domain

import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Prova a promessa da Clean Architecture: a regra roda na JVM, sem emulador automotivo,
 * com um fake do CarRepository. Ver docs/08.
 */
class ObserveVehicleSpeedTest {

    private class FakeCar(private val speeds: List<VehicleSpeed>) : CarRepository {
        override fun vehicleSpeed(): Flow<VehicleSpeed> = flowOf(*speeds.toTypedArray())
        override fun gear(): Flow<Gear> = flowOf(Gear.DRIVE)
        override fun energy(): Flow<Energy> = flowOf(Energy.Unavailable)
        override fun rangeKm(): Flow<Int> = flowOf(400)
        override fun outsideTempC(): Flow<Int> = flowOf(22)
        override fun climate(): Flow<com.autodash.core.model.Climate> = flowOf(com.autodash.core.model.Climate())
        override suspend fun setSeatTemp(seat: com.autodash.core.model.Seat, tempC: Float) {}
        override suspend fun setPower(on: Boolean) {}
        override suspend fun setAc(on: Boolean) {}
        override suspend fun setFan(speed: Int) {}
    }

    @Test
    fun distinctUntilChanged_removeLeiturasRepetidas() = runTest {
        val car = FakeCar(listOf(VehicleSpeed(10f), VehicleSpeed(10f), VehicleSpeed(20f)))
        val result = ObserveVehicleSpeed(car)().toList()
        assertEquals(listOf(VehicleSpeed(10f), VehicleSpeed(20f)), result)
    }

    @Test
    fun converteMetrosPorSegundo_paraKmh() {
        assertEquals(36, VehicleSpeed(10f).kmh)
    }
}
