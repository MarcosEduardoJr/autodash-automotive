package com.autodash.core.designsystem

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * WHITE-LABEL: a identidade da marca é DADO (tokens), não código hardcoded.
 * Nenhuma tela conhece "Volvo" ou "Scania" — ela lê tokens. Trocar de marca = trocar BrandTokens.
 * É o design system multi-brand que a vaga pede (dynamic theming, docs/06).
 */
data class BrandTokens(
    val id: String,
    val name: String,
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onBackground: Color,
    val onSurface: Color,
)

/** Marcas de exemplo com nomes NEUTROS (white-label) — de propósito NÃO são OEMs reais. */
object Brands {
    val Slate = BrandTokens("slate", "SLATE",
        Color(0xFF9CC2FF), Color(0xFF6E7B90), Color(0xFF0C0F14), Color(0xFF161A22),
        Color(0xFFE7ECF4), Color(0xFFC9D2DF))
    val Aurora = BrandTokens("aurora", "AURORA",
        Color(0xFF31E1B0), Color(0xFF1FA98A), Color(0xFF06120E), Color(0xFF0E1B16),
        Color(0xFFE6FBF3), Color(0xFFBFE9DC))
    val Ember = BrandTokens("ember", "EMBER",
        Color(0xFFFFB020), Color(0xFFB5730A), Color(0xFF120C05), Color(0xFF1E160A),
        Color(0xFFFBEFD9), Color(0xFFE8D3AC))
    val Nord = BrandTokens("nord", "NORD",
        Color(0xFF88C0FF), Color(0xFF5E81AC), Color(0xFF0A0E16), Color(0xFF141A26),
        Color(0xFFE6EEFB), Color(0xFFC2D2EA))

    val all: List<BrandTokens> = listOf(Slate, Aurora, Ember, Nord)
    fun byId(id: String?): BrandTokens = all.firstOrNull { it.id == id } ?: Slate
    fun next(current: BrandTokens): BrandTokens = all[(all.indexOf(current) + 1) % all.size]
}

/** Qualquer tela lê os tokens da marca ativa (nome, logo) por aqui. */
val LocalBrandTokens = staticCompositionLocalOf { Brands.Slate }
