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
)

/**
 * MVVM: expõe um StateFlow imutável; a UI só lê. NÃO conhece CarPropertyManager —
 * fala com use cases/repo do domínio. Assim testa na JVM com um fake do CarRepository.
 */
class DashboardViewModel(car: CarRepository) : ViewModel() {

    private val observeSpeed = ObserveVehicleSpeed(car)
    private val _ui = MutableStateFlow(DashboardUi())
    val ui: StateFlow<DashboardUi> = _ui.asStateFlow()

    init {
        viewModelScope.launch { observeSpeed().collect { s -> _ui.value = _ui.value.copy(speed = s) } }
        viewModelScope.launch { car.gear().collect { g -> _ui.value = _ui.value.copy(gear = g) } }
        viewModelScope.launch { car.energy().collect { e -> _ui.value = _ui.value.copy(energy = e) } }
    }
}
