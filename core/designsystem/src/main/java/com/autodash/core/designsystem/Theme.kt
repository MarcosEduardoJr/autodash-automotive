package com.autodash.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * Tema white-label + DIA/NOITE. Escuro usa os tokens da marca; claro usa o accent da marca
 * (secondary, com contraste melhor no fundo claro) sobre neutros claros. Em produção, o
 * dark/light seguiria a propriedade NIGHT_MODE do veículo (VHAL); aqui segue o sistema.
 * DYNAMIC THEMING: trocar os tokens re-tematiza tudo.
 */
@Composable
fun AutoDashTheme(
    tokens: BrandTokens = Brands.Slate,
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val scheme = if (dark) {
        darkColorScheme(
            primary = tokens.primary,
            secondary = tokens.secondary,
            background = tokens.background,
            surface = tokens.surface,
            surfaceVariant = tokens.surface,
            onBackground = tokens.onBackground,
            onSurface = tokens.onSurface,
            onSurfaceVariant = tokens.onSurface,
            onPrimary = tokens.background,
        )
    } else {
        lightColorScheme(
            primary = tokens.secondary,          // mais escuro → contrasta no claro
            onPrimary = Color(0xFFFFFFFF),
            secondary = tokens.primary,
            background = Color(0xFFF2F5FA),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFE7ECF4),
            onBackground = Color(0xFF141A22),
            onSurface = Color(0xFF141A22),
            onSurfaceVariant = Color(0xFF5B6676),
        )
    }
    CompositionLocalProvider(LocalBrandTokens provides tokens) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
