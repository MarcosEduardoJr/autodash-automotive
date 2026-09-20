package com.autodash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.autodash.data.car.FakeCarRepository
import com.autodash.domain.CarRepository
import com.autodash.feature.dashboard.DashboardScreen
import com.autodash.feature.dashboard.DashboardViewModel

/**
 * Host do DASHBOARD (parked-optimized). Ver docs/02 e docs/05.
 *
 * DEMO: usamos FakeCarRepository → roda em qualquer emulador, sem permissão/carro real.
 * Em AAOS real, troque a linha do repo por:
 *   val car = android.car.Car.createCar(this)
 *   val repo = com.autodash.data.car.CarPropertyRepository(car)
 * O resto (ViewModel, UI) não muda: é o poder do contrato CarRepository (Clean Arch).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo: CarRepository = FakeCarRepository()
        setContent {
            val vm: DashboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        DashboardViewModel(repo) as T
                }
            )
            val state by vm.ui.collectAsStateWithLifecycle()
            DashboardScreen(state)
        }
    }
}
