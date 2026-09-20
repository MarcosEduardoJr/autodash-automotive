package com.autodash.domain

import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import kotlinx.coroutines.flow.Flow

/**
 * CONTRATO com o veículo — o domínio depende DISTO, não do CarPropertyManager.
 * data/car implementa usando a Car API; testes usam um fake. É o coração da
 * inversão de dependência que mantém a regra testável na JVM.
 */
interface CarRepository {
    fun vehicleSpeed(): Flow<VehicleSpeed>
    fun gear(): Flow<Gear>
    fun energy(): Flow<Energy>
}
