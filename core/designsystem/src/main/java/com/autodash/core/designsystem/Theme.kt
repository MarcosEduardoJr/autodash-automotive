package com.autodash.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Aplica os tokens da marca ao Material3 e publica LocalBrandTokens.
 * DYNAMIC THEMING: mudar 'tokens' re-tematiza TUDO (as telas leem colorScheme + LocalBrandTokens),
 * sem recompilar nem tocar nas telas. O RRO da OEM pode sobrepor por cima em runtime (docs/06).
 */
@Composable
fun AutoDashTheme(
    tokens: BrandTokens = Brands.Slate,
    content: @Composable () -> Unit,
) {
    val scheme = darkColorScheme(
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
    CompositionLocalProvider(LocalBrandTokens provides tokens) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
