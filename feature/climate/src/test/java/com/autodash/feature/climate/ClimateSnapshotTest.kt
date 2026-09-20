package com.autodash.feature.climate

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.autodash.core.designsystem.AutoDashTheme
import com.autodash.core.designsystem.Brands
import com.autodash.core.model.Climate
import org.junit.Rule
import org.junit.Test

/** Snapshot da tela de clima por marca (HVAC por zona). */
class ClimateSnapshotTest {
    @get:Rule val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_C)

    @Test fun climate_multiBrand() {
        listOf(Brands.Slate, Brands.Aurora).forEach { b ->
            paparazzi.snapshot(name = "climate_${b.id}") {
                AutoDashTheme(b) { ClimateScreen(Climate(driverC = 22f, passengerC = 20f)) }
            }
        }
    }
}
