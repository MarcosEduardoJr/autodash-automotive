package com.autodash.data.car

import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import com.autodash.domain.CarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Repositório FALSO — simula sinais do veículo. Serve para:
 *  - rodar o app em QUALQUER emulador, sem permissões nem carro real;
 *  - testes na JVM.
 * Mesmo contrato do domínio → trocar fake↔real (CarPropertyRepository) não toca a UI (docs/08).
 */
class FakeCarRepository : CarRepository {

    override fun vehicleSpeed(): Flow<VehicleSpeed> = flow {
        var mps = 0f
        var up = true
        while (true) {
            emit(VehicleSpeed(mps))
            mps += if (up) 1.5f else -1.5f
            if (mps >= 33f) up = false      // ~120 km/h
            if (mps <= 0f) up = true
            delay(350)
        }
    }

    override fun gear(): Flow<Gear> = flowOf(Gear.DRIVE)

    override fun energy(): Flow<Energy> = flow {
        var pct = 82
        while (true) {
            emit(Energy.Battery(pct))
            pct = if (pct <= 6) 82 else pct - 1
            delay(2500)
        }
    }
}
