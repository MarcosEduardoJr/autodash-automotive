package com.autodash.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Tema base. RRO-FRIENDLY: as telas consomem MaterialTheme.colorScheme.* (tokens),
 * nunca cores fixas — assim o overlay da montadora consegue re-vestir o app (docs/06).
 * Head unit costuma operar em escuro; o esquema real vem do tema/flavor/RRO.
 */
@Composable
fun AutoDashTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = darkColorScheme(), content = content)
}
