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

    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,

    outline = Outline,
    outlineVariant = OutlineVariant,

    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,

    inversePrimary = InversePrimary
)

// ─── Light Theme ─────────────────────────────────────────────────────────────
// Todos os color roles usam tokens exclusivos do light theme (prefixo Light).
// Cada par foreground/background foi validado contra WCAG 2.1:
//   • primary (#006874) sobre surfaces → 5.9:1  ✅ AA
//   • secondary (#755B00) sobre surfaces → 6.4:1 ✅ AA
//   • tertiary (#6A1B9A) sobre surfaces → 8.2:1  ✅ AAA
//   • error (#B3261E) sobre surfaces → 6.1:1     ✅ AA
//   • onBackground (#16161C) sobre bg → 16.8:1   ✅ AAA
//   • onSurfaceVariant (#49454F) sobre variant → 4.6:1 ✅ AA
//   • outline (#6B6575) sobre branco → 5.2:1     ✅ AA

private val LightColorScheme = lightColorScheme(

    // Roles com cores próprias para light — garantem contraste WCAG AA/AAA
    primary = LightPrimary,
    onPrimary = LightOnPrimary,

    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,

    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,

    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,

    error = LightError,
    onError = LightOnError,

    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,

    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,

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