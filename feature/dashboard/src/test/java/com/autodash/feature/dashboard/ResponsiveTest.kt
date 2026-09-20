package com.autodash.feature.dashboard

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Prova a decisão responsiva sem device (a UI usa a mesma isWide via BoxWithConstraints). */
class ResponsiveTest {
    @Test fun landscape_isWide() = assertTrue(isWide(1408, 792))
    @Test fun portrait_isNotWide() = assertFalse(isWide(792, 1408))
    @Test fun square_countsAsWide() = assertTrue(isWide(800, 800))
}
