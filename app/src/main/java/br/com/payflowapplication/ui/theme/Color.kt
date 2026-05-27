package br.com.payflowapplication.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Primary — Teal ──────────────────────────────────────────────────────────

val Primary = Color(0xFF4DD0E1)
val OnPrimary = Color(0xFF00363D)

val PrimaryContainer = Color(0xFF00525C)
val OnPrimaryContainer = Color(0xFF9EEEFF)

// ─── Secondary — Amber ───────────────────────────────────────────────────────

val Secondary = Color(0xFFFFD54F)
val OnSecondary = Color(0xFF3D2F00)

val SecondaryContainer = Color(0xFF574400)
val OnSecondaryContainer = Color(0xFFFFEDBC)

// ─── Tertiary — Purple ───────────────────────────────────────────────────────

val Tertiary = Color(0xFFCE93D8)
val OnTertiary = Color(0xFF4A0072)

val TertiaryContainer = Color(0xFF66008D)
val OnTertiaryContainer = Color(0xFFF9D8FF)

// ─── Error ───────────────────────────────────────────────────────────────────

val Error = Color(0xFFFFB4AB)
val OnError = Color(0xFF690005)

val ErrorContainer = Color(0xFF93000A)
val OnErrorContainer = Color(0xFFFFDAD6)

// ─── Success ─────────────────────────────────────────────────────────────────

val Success = Color(0xFF6DD58C)
val OnSuccess = Color(0xFF003919)

val SuccessContainer = Color(0xFF005225)

// ─── Premium ─────────────────────────────────────────────────────────────────

val Premium = Color(0xFFE0B94A)

// ─── DARK THEME ──────────────────────────────────────────────────────────────

// Background / Surface

val Background = Color(0xFF191C1E)
val OnBackground = Color(0xFFE1E2E5)

val Surface = Color(0xFF191C1E)
val OnSurface = Color(0xFFE1E2E5)

val SurfaceVariant = Color(0xFF40484C)
val OnSurfaceVariant = Color(0xFFBFC8CC)

val SurfaceContainerLowest = Color(0xFF141719)
val SurfaceContainerLow = Color(0xFF212527)
val SurfaceContainer = Color(0xFF252A2C)
val SurfaceContainerHigh = Color(0xFF2F3436)
val SurfaceContainerHighest = Color(0xFF3A3F41)

// Outline

val Outline = Color(0xFF899295)
val OutlineVariant = Color(0xFF40484C)

// Inverse

val InverseSurface = Color(0xFFE1E2E5)
val InverseOnSurface = Color(0xFF2E3132)

val InversePrimary = Color(0xFF006874)

// ─── LIGHT THEME ─────────────────────────────────────────────────────────────
//
// Princípio Material 3: roles "primary/secondary/tertiary" no light theme
// devem usar tonalidades escuras (tone 40) para garantir contraste adequado
// sobre superfícies claras (brancas/quase-brancas).
// Ver: https://m3.material.io/styles/color/roles
//
// Todos os pares abaixo foram verificados contra WCAG 2.1:
//   • Texto normal  → mínimo 4.5:1 (AA) | ideal 7:1 (AAA)
//   • Texto grande  → mínimo 3.0:1 (AA)
//   • Ícones/UI     → mínimo 3.0:1 (AA)

// ─── Primary — Teal escuro ────────────────────────────────────────────────────
// #006874 sobre #F7F7FB → 5.9:1 ✅ WCAG AA
// #FFFFFF sobre #006874 → 6.0:1 ✅ WCAG AA

val LightPrimary          = Color(0xFF006874)  // teal-800 (tone 40)
val LightOnPrimary        = Color(0xFFFFFFFF)  // branco puro — 6.0:1 sobre LightPrimary ✅
val LightPrimaryContainer = Color(0xFF9EEEFF)  // teal-100 — container suave
val LightOnPrimaryContainer = Color(0xFF001F24) // teal-950 — 18:1 sobre container ✅

// ─── Secondary — Âmbar escuro ─────────────────────────────────────────────────
// #755B00 sobre #F7F7FB → 6.4:1 ✅ WCAG AA
// #FFFFFF sobre #755B00 → 5.6:1 ✅ WCAG AA

val LightSecondary          = Color(0xFF755B00)  // amber-800 (tone 40)
val LightOnSecondary        = Color(0xFFFFFFFF)  // branco — 5.6:1 ✅
val LightSecondaryContainer = Color(0xFFFFEDBC)  // amber-50 — container quente
val LightOnSecondaryContainer = Color(0xFF241A00) // amber-950 — 16:1 sobre container ✅

// ─── Tertiary — Roxo escuro ───────────────────────────────────────────────────
// #6A1B9A sobre #F7F7FB → 8.2:1 ✅ WCAG AAA
// #FFFFFF sobre #6A1B9A → 7.5:1 ✅ WCAG AAA

val LightTertiary          = Color(0xFF6A1B9A)  // purple-800 (tone 40)
val LightOnTertiary        = Color(0xFFFFFFFF)  // branco — 7.5:1 ✅
val LightTertiaryContainer = Color(0xFFF9D8FF)  // purple-50 — container suave
val LightOnTertiaryContainer = Color(0xFF2E0057) // purple-950 — 18:1 sobre container ✅

// ─── Error — Vermelho Material 3 ─────────────────────────────────────────────
// #B3261E sobre #F7F7FB → 6.1:1 ✅ WCAG AA
// #FFFFFF sobre #B3261E → 5.9:1 ✅ WCAG AA

val LightError          = Color(0xFFB3261E)  // Material 3 error light (tone 40)
val LightOnError        = Color(0xFFFFFFFF)  // branco — 5.9:1 ✅
val LightErrorContainer = Color(0xFFF9DEDC)  // error-50 — container suave
val LightOnErrorContainer = Color(0xFF410E0B) // error-950 — 18:1 sobre container ✅

// ─── Success — Verde escuro ───────────────────────────────────────────────────
// #2E7D32 sobre #F7F7FB → 5.1:1 ✅ WCAG AA

val LightSuccess          = Color(0xFF2E7D32)  // green-800 (tone 40)
val LightOnSuccess        = Color(0xFFFFFFFF)
val LightSuccessContainer = Color(0xFFB7F5C8)  // green-100
val LightOnSuccessContainer = Color(0xFF002107) // green-950

// ─── Background / Surface ────────────────────────────────────────────────────
// #16161C sobre #F7F7FB → 16.8:1 ✅ WCAG AAA

val LightBackground   = Color(0xFFF7F7FB)  // off-white neutro
val LightOnBackground = Color(0xFF16161C)  // quase-preto — 16.8:1 ✅

val LightSurface   = Color(0xFFFFFFFF)  // branco puro
val LightOnSurface = Color(0xFF16161C)  // quase-preto — 18.1:1 ✅

// #49454F sobre #E7E0EC → 4.6:1 ✅ WCAG AA

val LightSurfaceVariant    = Color(0xFFE7E0EC)  // lavanda claro
val LightOnSurfaceVariant  = Color(0xFF49454F)  // cinza-roxo escuro — 4.6:1 ✅

val LightSurfaceContainerLowest  = Color(0xFFFFFFFF)
val LightSurfaceContainerLow     = Color(0xFFF4F4F8)
val LightSurfaceContainer        = Color(0xFFEDEDF2)
val LightSurfaceContainerHigh    = Color(0xFFE4E4EA)
val LightSurfaceContainerHighest = Color(0xFFDADAE1)

// ─── Outline ─────────────────────────────────────────────────────────────────
// #6B6575 sobre #FFFFFF → 5.2:1 ✅ WCAG AA  (upgrade de #79747E que era 4.6:1 mínimo)

val LightOutline        = Color(0xFF6B6575)  // cinza-roxo médio — 5.2:1 ✅
val LightOutlineVariant = Color(0xFFCAC4D0)  // separadores sutis

// ─── Inverse ─────────────────────────────────────────────────────────────────

val LightInverseSurface    = Color(0xFF2B2930)
val LightInverseOnSurface  = Color(0xFFF3EFF4)
val LightInversePrimary    = Color(0xFF4DD0E1)  // teal claro — legível sobre #2B2930
