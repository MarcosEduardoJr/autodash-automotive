package com.autodash.feature.dashboard

import com.autodash.core.designsystem.Brands
import org.junit.Assert.assertEquals
import org.junit.Test

/** White-label: o registro de marcas é consistente e o ciclo do seletor funciona. */
class BrandTest {
    @Test fun registryTemMarcasComIdsUnicos() {
        assertEquals(4, Brands.all.size)
        assertEquals(Brands.all.size, Brands.all.map { it.id }.distinct().size)
    }
    @Test fun nextDaVoltaNoFim() {
        assertEquals(Brands.all.first(), Brands.next(Brands.all.last()))
    }
    @Test fun byIdCaiNoSlateQuandoNaoExiste() {
        assertEquals(Brands.Slate, Brands.byId("inexistente"))
    }
}
