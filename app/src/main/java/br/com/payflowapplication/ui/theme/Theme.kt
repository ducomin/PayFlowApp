package br.com.payflowapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ─── Dark Theme ──────────────────────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(

    primary = Primary,
    onPrimary = OnPrimary,

    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary = Secondary,
    onSecondary = OnSecondary,

    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = Tertiary,
    onTertiary = OnTertiary,

    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    error = Error,
    onError = OnError,

    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,

    background = Background,
    onBackground = OnBackground,

    surface = Surface,
    onSurface = OnSurface,

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    outline = Outline,
    outlineVariant = OutlineVariant,

    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,

    inversePrimary = InversePrimary
)

// ─── Light Theme ─────────────────────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(

    primary = Primary,
    onPrimary = OnPrimary,

    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary = Secondary,
    onSecondary = OnSecondary,

    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = Tertiary,
    onTertiary = OnTertiary,

    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    error = Error,
    onError = OnError,

    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,

    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,

    inversePrimary = LightInversePrimary
)

// ─── Theme ───────────────────────────────────────────────────────────────────

@Composable
fun PayFlowTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },

        content = content
    )
}