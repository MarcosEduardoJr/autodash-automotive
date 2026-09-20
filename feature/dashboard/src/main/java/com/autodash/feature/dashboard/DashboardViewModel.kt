package com.autodash.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import com.autodash.domain.CarRepository
import com.autodash.domain.ObserveVehicleSpeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUi(
    val speed: VehicleSpeed = VehicleSpeed(0f),
    val gear: Gear = Gear.UNKNOWN,
    val energy: Energy = Energy.Unavailable,
    val rangeKm: Int = 0,
    val outsideTempC: Int = 0,
)

/** MVVM: StateFlow imutável; a UI só lê. Não conhece CarPropertyManager (fala com o domínio). */
class DashboardViewModel(car: CarRepository) : ViewModel() {

    private val observeSpeed = ObserveVehicleSpeed(car)
    private val _ui = MutableStateFlow(DashboardUi())
    val ui: StateFlow<DashboardUi> = _ui.asStateFlow()

    init {
        viewModelScope.launch { observeSpeed().collect { _ui.value = _ui.value.copy(speed = it) } }
        viewModelScope.launch { car.gear().collect { _ui.value = _ui.value.copy(gear = it) } }
        viewModelScope.launch { car.energy().collect { _ui.value = _ui.value.copy(energy = it) } }
        viewModelScope.launch { car.rangeKm().collect { _ui.value = _ui.value.copy(rangeKm = it) } }
        viewModelScope.launch { car.outsideTempC().collect { _ui.value = _ui.value.copy(outsideTempC = it) } }
    }
}
