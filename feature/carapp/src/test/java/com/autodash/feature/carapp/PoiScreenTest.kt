package com.autodash.feature.carapp

import androidx.car.app.model.ListTemplate
import androidx.car.app.model.PaneTemplate
import androidx.car.app.testing.TestCarContext
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Teste do TEMPLATE renderizado (instrumentação-like via Robolectric + car.app:app-testing).
 * Sem host de carro real: TestCarContext simula o CarContext.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PoiScreenTest {

    private val carContext =
        TestCarContext.createCarContext(ApplicationProvider.getApplicationContext())

    @Test
    fun poiScreen_listaTodosOsPois() {
        val template = PoiScreen(carContext).onGetTemplate() as ListTemplate
        val items = template.singleList!!.items
        assertEquals(demoPois.size, items.size)
    }

    @Test
    fun detalhe_ehPaneTemplate() {
        val template = PoiDetailScreen(carContext, demoPois.first()).onGetTemplate()
        assertTrue(template is PaneTemplate)
    }
}
