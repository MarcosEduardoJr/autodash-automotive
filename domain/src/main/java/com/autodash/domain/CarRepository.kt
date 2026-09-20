package com.autodash.domain

import com.autodash.core.model.Energy
import com.autodash.core.model.Climate
import com.autodash.core.model.Gear
import com.autodash.core.model.Seat
import com.autodash.core.model.VehicleSpeed
import kotlinx.coroutines.flow.Flow

/**
 * CONTRATO com o veículo — o domínio depende DISTO, não do CarPropertyManager.
 * data/car implementa via Car API; testes usam um fake. Inversão de dependência.
 */
interface CarRepository {
    fun vehicleSpeed(): Flow<VehicleSpeed>
    fun gear(): Flow<Gear>
    fun energy(): Flow<Energy>
    fun rangeKm(): Flow<Int>        // autonomia restante (RANGE_REMAINING no VHAL)
    fun outsideTempC(): Flow<Int>   // temperatura externa (ENV_OUTSIDE_TEMPERATURE)

    fun climate(): Flow<Climate>                       // HVAC por zona (HVAC_TEMPERATURE_SET por area)
    suspend fun setSeatTemp(seat: Seat, tempC: Float)  // escreve HVAC (exige CONTROL_CAR_CLIMATE, docs/04)
}
