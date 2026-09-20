package com.autodash.feature.climate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autodash.core.model.Climate
import com.autodash.core.model.Seat
import com.autodash.domain.CarRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * HVAC por zona. A UI lê o StateFlow e manda deltas; o repo escreve no veículo
 * (setProperty por area — exige CONTROL_CAR_CLIMATE). Não conhece a Car API.
 */
class ClimateViewModel(private val car: CarRepository) : ViewModel() {

    val ui: StateFlow<Climate> =
        car.climate().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Climate())

    fun delta(seat: Seat, deltaC: Float) {
        viewModelScope.launch { car.setSeatTemp(seat, ui.value.temp(seat) + deltaC) }
    }
}
