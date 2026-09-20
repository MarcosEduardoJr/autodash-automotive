package com.autodash.feature.dashboard

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.ScreenOrientation
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.designsystem.Brands
import com.autodash.core.model.Energy
import com.autodash.core.model.Gear
import com.autodash.core.model.VehicleSpeed
import org.junit.Rule
import org.junit.Test

/**
 * SNAPSHOT MULTI-BRAND (o que a vaga pede): renderiza o dashboard em CADA marca e em
 * landscape/portrait, na JVM, sem device. O CI compara com os goldens e barra regressão visual.
 */
class DashboardSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_C) // tela larga

    private val state = DashboardUi(VehicleSpeed(23f), Gear.DRIVE, Energy.Battery(74))

    @Test
    fun dashboard_multiBrand_landscape() {
        Brands.all.forEach { brand ->
            paparazzi.snapshot(name = "landscape_${brand.id}") {
                AutoDashTheme(brand) { DashboardScreen(state) }
            }
        }
    }

    @Test
    fun dashboard_portrait() {
        paparazzi.unsafeUpdateConfig(
            DeviceConfig.PIXEL_C.copy(orientation = ScreenOrientation.PORTRAIT),
        )
        paparazzi.snapshot(name = "portrait_slate") {
            AutoDashTheme(Brands.Slate) { DashboardScreen(state) }
        }
    }

    @Test
    fun dashboard_english_imperial() {
        // i18n: locale en-US -> mph + strings em inglês (values-en). Ver docs/09.
        paparazzi.unsafeUpdateConfig(DeviceConfig.PIXEL_C.copy(locale = "en-rUS"))
        paparazzi.snapshot(name = "english_imperial_slate") {
            AutoDashTheme(Brands.Slate) { DashboardScreen(state) }
        }
    }
}
