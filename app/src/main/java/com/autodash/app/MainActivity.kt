package com.autodash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.autodash.feature.dashboard.DashboardScreen

/**
 * Host do DASHBOARD (parked-optimized).
 *
 * Mostra telemetria do veículo (velocidade/marcha/energia) lida via CarPropertyManager
 * (ver data/car e docs/02). Em Compose. A experiência é pensada para o carro PARADO;
 * em movimento, o CarUxRestrictions manda enxugar (docs/05).
 *
 * A superfície DIRIGÍVEL (POI) não é uma Activity — é um CarAppService (feature/carapp).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DashboardScreen() }
    }
}
