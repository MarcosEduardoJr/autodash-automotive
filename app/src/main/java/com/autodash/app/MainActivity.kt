package com.autodash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.designsystem.Brands
import com.autodash.data.car.FakeCarRepository
import com.autodash.domain.CarRepository
import com.autodash.feature.dashboard.DashboardScreen
import com.autodash.feature.dashboard.DashboardViewModel

/**
 * Host do DASHBOARD. Duas coisas novas:
 *  - WHITE-LABEL: a marca vem de BuildConfig.DEFAULT_BRAND (config, não código); o seletor
 *    troca em runtime (DYNAMIC THEMING) — mudar o BrandTokens re-tematiza tudo.
 *  - Responsivo: DashboardScreen adapta portrait/landscape sozinho.
 *
 * DEMO: FakeCarRepository roda em qualquer emulador. Em AAOS real, troque por:
 *   CarPropertyRepository(android.car.Car.createCar(this))  (a UI não muda — Clean Arch).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo: CarRepository = FakeCarRepository()
        setContent {
            var brandId by rememberSaveable { mutableStateOf(BuildConfig.DEFAULT_BRAND) }
            val brand = Brands.byId(brandId)
            AutoDashTheme(brand) {
                val vm: DashboardViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T =
                            DashboardViewModel(repo) as T
                    },
                )
                val state by vm.ui.collectAsStateWithLifecycle()
                DashboardScreen(state, onCycleBrand = { brandId = Brands.next(brand).id })
            }
        }
    }
}
