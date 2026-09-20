package com.autodash.core.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

/** i18n/l10n: a unidade segue o locale, e a conversão de velocidade está correta. */
class UnitsTest {
    @Test fun eua_usaMilhas() =
        assertEquals(UnitSystem.IMPERIAL, unitSystemForLocale(Locale("en", "US")))

    @Test fun brasil_usaKm() =
        assertEquals(UnitSystem.METRIC, unitSystemForLocale(Locale("pt", "BR")))

    @Test fun converteVelocidade() {
        val s = VehicleSpeed(10f) // 10 m/s
        assertEquals(36, s.speedIn(UnitSystem.METRIC))   // km/h
        assertEquals(22, s.speedIn(UnitSystem.IMPERIAL)) // mph
    }
}
