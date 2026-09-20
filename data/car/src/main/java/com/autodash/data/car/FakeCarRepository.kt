package com.autodash.data.car

import com.autodash.core.model.Climate
import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import com.autodash.core.model.Seat
import com.autodash.domain.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Repositório FALSO — simula sinais do veículo. Roda em qualquer emulador (sem carro/permissão)
 * e nos testes. Mesmo contrato do domínio → trocar por CarPropertyRepository não toca a UI.
 */
class FakeCarRepository : CarRepository {

    override fun vehicleSpeed(): Flow<VehicleSpeed> = flow {
        var mps = 0f; var up = true
        while (true) {
            emit(VehicleSpeed(mps))
            mps += if (up) 1.5f else -1.5f
            if (mps >= 33f) up = false
            if (mps <= 0f) up = true
            delay(350)
        }
    }

    override fun gear(): Flow<Gear> = flowOf(Gear.DRIVE)

    override fun energy(): Flow<Energy> = flow {
        var pct = 82
        while (true) { emit(Energy.Battery(pct)); pct = if (pct <= 6) 82 else pct - 1; delay(2500) }
    }

    override fun rangeKm(): Flow<Int> = flow {
        var km = 418
        while (true) { emit(km); km = if (km <= 360) 418 else km - 1; delay(3500) }
    }

    override fun outsideTempC(): Flow<Int> = flow {
        var t = 22; var up = true
        while (true) { emit(t); t += if (up) 1 else -1; if (t >= 26) up = false; if (t <= 18) up = true; delay(6000) }
    }


    private val _climate = MutableStateFlow(Climate())
    override fun climate(): Flow<Climate> = _climate
    override suspend fun setSeatTemp(seat: Seat, tempC: Float) {
        val t = tempC.coerceIn(16f, 28f)
        _climate.value =
            if (seat == Seat.DRIVER) _climate.value.copy(driverC = t)
            else _climate.value.copy(passengerC = t)
    }
    override suspend fun setPower(on: Boolean) { _climate.value = _climate.value.copy(powerOn = on) }
    override suspend fun setAc(on: Boolean) { _climate.value = _climate.value.copy(acOn = on) }
    override suspend fun setFan(speed: Int) { _climate.value = _climate.value.copy(fanSpeed = speed.coerceIn(0, 6)) }
}
