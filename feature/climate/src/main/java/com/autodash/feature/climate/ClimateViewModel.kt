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

/** HVAC completo por zona. A UI lê o StateFlow e manda comandos; o repo escreve no veículo. */
class ClimateViewModel(private val car: CarRepository) : ViewModel() {

    val ui: StateFlow<Climate> =
        car.climate().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Climate())

    fun delta(seat: Seat, deltaC: Float) = viewModelScope.launch { car.setSeatTemp(seat, ui.value.temp(seat) + deltaC) }
    fun togglePower() = viewModelScope.launch { car.setPower(!ui.value.powerOn) }
    fun toggleAc() = viewModelScope.launch { car.setAc(!ui.value.acOn) }
    fun fan(delta: Int) = viewModelScope.launch { car.setFan(ui.value.fanSpeed + delta) }
}
