package com.autodash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.designsystem.Brands
import com.autodash.core.designsystem.ChakraPetch
import com.autodash.core.designsystem.LocalBrandTokens
import com.autodash.data.car.FakeCarRepository
import com.autodash.domain.CarRepository
import com.autodash.feature.climate.ClimateScreen
import com.autodash.feature.climate.ClimateViewModel
import com.autodash.feature.dashboard.DashboardScreen
import com.autodash.feature.dashboard.DashboardViewModel

/**
 * Host. Chrome do app (marca + nav Cluster/Clima) + as telas (headerless).
 * WHITE-LABEL: marca fixa por build (BuildConfig.DEFAULT_BRAND). DEMO: FakeCarRepository.
 * Em AAOS real: CarPropertyRepository(android.car.Car.createCar(this)).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo: CarRepository = FakeCarRepository()
        setContent {
            AutoDashTheme(Brands.byId(BuildConfig.DEFAULT_BRAND)) {
                var tab by rememberSaveable { mutableIntStateOf(0) }
                val dashVm: DashboardViewModel = viewModel(factory = factory { DashboardViewModel(repo) })
                val climVm: ClimateViewModel = viewModel(factory = factory { ClimateViewModel(repo) })
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(Modifier.fillMaxSize()) {
                        AppBar(tab, onTab = { tab = it })
                        Box(Modifier.weight(1f)) {
                            if (tab == 0) {
                                val s by dashVm.ui.collectAsStateWithLifecycle()
                                DashboardScreen(s)
                            } else {
                                val c by climVm.ui.collectAsStateWithLifecycle()
                                ClimateScreen(c, onDelta = climVm::delta, onPower = climVm::togglePower, onAc = climVm::toggleAc, onFan = climVm::fan)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Barra do app: identidade da marca + navegação. Chrome fica no app, não nas telas. */
@Composable
private fun AppBar(tab: Int, onTab: (Int) -> Unit) {
    val cs = MaterialTheme.colorScheme
    val brand = LocalBrandTokens.current
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(38.dp).clip(RoundedCornerShape(11.dp))
                .background(Brush.linearGradient(listOf(cs.primary, cs.primary.copy(alpha = 0.6f)))),
            contentAlignment = Alignment.Center,
        ) {
            Text(brand.name.take(1), color = cs.onPrimary, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, fontSize = 19.sp)
        }
        Spacer(Modifier.width(13.dp))
        Column {
            Text("AUTODASH", color = cs.onBackground, fontFamily = ChakraPetch,
                fontWeight = FontWeight.Bold, letterSpacing = 4.sp, fontSize = 15.sp)
            Text(brand.name, color = cs.onSurfaceVariant, fontFamily = ChakraPetch,
                letterSpacing = 3.sp, fontSize = 10.sp)
        }
        Spacer(Modifier.width(28.dp))
        NavPill("CLUSTER", tab == 0) { onTab(0) }
        Spacer(Modifier.width(8.dp))
        NavPill("CLIMA", tab == 1) { onTab(1) }
        Spacer(Modifier.weight(1f))
        Box(Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(cs.primary))
        Spacer(Modifier.width(7.dp))
        Text("LIVE", color = cs.primary, fontFamily = ChakraPetch,
            fontWeight = FontWeight.SemiBold, letterSpacing = 3.sp, fontSize = 11.sp)
    }
}

@Composable
private fun NavPill(label: String, on: Boolean, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box(
        Modifier.clip(RoundedCornerShape(10.dp))
            .background(if (on) cs.primary else cs.surface)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 9.dp),
    ) {
        Text(label, color = if (on) cs.onPrimary else cs.onSurfaceVariant,
            fontFamily = ChakraPetch, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp, fontSize = 13.sp)
    }
}

private inline fun <VM : ViewModel> factory(crossinline create: () -> VM) =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
    }
