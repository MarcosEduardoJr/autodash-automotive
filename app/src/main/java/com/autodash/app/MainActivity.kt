package com.autodash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
 * Host. Navegação por **NavigationRail** (padrão automotivo em landscape: alvo grande,
 * foco/rotativo, TalkBack). White-label: marca do FLAVOR (BuildConfig.DEFAULT_BRAND).
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
                    Row(Modifier.fillMaxSize()) {
                        Rail(tab, onTab = { tab = it })
                        Box(Modifier.weight(1f)) {
                            if (tab == 0) {
                                val s by dashVm.ui.collectAsStateWithLifecycle()
                                DashboardScreen(s)
                            } else {
                                val c by climVm.ui.collectAsStateWithLifecycle()
                                ClimateScreen(c, onDelta = climVm::delta, onPower = climVm::togglePower,
                                    onAc = climVm::toggleAc, onFan = climVm::fan)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Rail(tab: Int, onTab: (Int) -> Unit) {
    val cs = MaterialTheme.colorScheme
    val brand = LocalBrandTokens.current
    NavigationRail(
        header = {
            Box(
                Modifier.padding(top = 8.dp).size(44.dp).clip(RoundedCornerShape(13.dp))
                    .background(Brush.linearGradient(listOf(cs.primary, cs.primary.copy(alpha = 0.6f))))
                    .semantics { contentDescription = "AutoDash ${brand.name}" },
                contentAlignment = Alignment.Center,
            ) {
                Text(brand.name.take(1), color = cs.onPrimary, fontFamily = ChakraPetch,
                    fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
    ) {
        Spacer(Modifier.height(12.dp))
        val colors = NavigationRailItemDefaults.colors(
            selectedIconColor = cs.onPrimary, indicatorColor = cs.primary,
            unselectedIconColor = cs.onSurfaceVariant,
            selectedTextColor = cs.primary, unselectedTextColor = cs.onSurfaceVariant,
        )
        NavigationRailItem(
            selected = tab == 0, onClick = { onTab(0) }, colors = colors,
            icon = { Icon(Icons.Filled.Speed, contentDescription = "Cluster") },
            label = { Text("Cluster", fontFamily = ChakraPetch, fontSize = 11.sp) },
        )
        NavigationRailItem(
            selected = tab == 1, onClick = { onTab(1) }, colors = colors,
            icon = { Icon(Icons.Filled.AcUnit, contentDescription = "Clima") },
            label = { Text("Clima", fontFamily = ChakraPetch, fontSize = 11.sp) },
        )
        Spacer(Modifier.weight(1f))
        Box(Modifier.size(9.dp).clip(RoundedCornerShape(5.dp)).background(cs.primary))
        Text("LIVE", color = cs.primary, fontFamily = ChakraPetch, fontSize = 9.sp,
            letterSpacing = 2.sp, modifier = Modifier.padding(bottom = 12.dp, top = 4.dp))
    }
}

private inline fun <VM : ViewModel> factory(crossinline create: () -> VM) =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
    }
