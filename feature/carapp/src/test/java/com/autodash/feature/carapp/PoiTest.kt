package com.autodash.feature.carapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Dados de POI consistentes (a UI dirigível depende disso). */
class PoiTest {
    @Test fun listaNaoVazia() = assertTrue(demoPois.isNotEmpty())

    @Test fun nomesUnicos() =
        assertEquals(demoPois.size, demoPois.map { it.name }.distinct().size)

    @Test fun todosComEndereco() =
        assertTrue(demoPois.all { it.address.isNotBlank() })
}
