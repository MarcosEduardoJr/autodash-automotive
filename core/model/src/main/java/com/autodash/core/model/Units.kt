package com.autodash.core.model

import java.util.Locale

/** Sistema de unidades. O label é símbolo universal (não se traduz). */
enum class UnitSystem(val speedLabel: String) { METRIC("km/h"), IMPERIAL("mph") }

/** Velocidade na unidade do sistema. VHAL entrega m/s; convertemos aqui. */
fun VehicleSpeed.speedIn(system: UnitSystem): Int =
    if (system == UnitSystem.IMPERIAL) mph else kmh

/** Países que usam milhas na via. O resto do mundo = métrico. */
private val IMPERIAL_COUNTRIES = setOf("US", "GB", "MM", "LR")

/**
 * i18n: escolhe a unidade pelo país do locale (não pelo idioma).
 * Caminhão em São Paulo → km/h; nos EUA/UK → mph. Puro → testável na JVM.
 */
fun unitSystemForLocale(locale: Locale): UnitSystem =
    if (locale.country.uppercase(Locale.ROOT) in IMPERIAL_COUNTRIES) UnitSystem.IMPERIAL
    else UnitSystem.METRIC
