package lt.vitalijus.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = DeepBrand500,
    onPrimary = DeepBrand1000,
    primaryContainer = DeepBrand100,
    onPrimaryContainer = DeepBrand900,

    secondary = DeepBase700,
    onSecondary = DeepBase0,
    secondaryContainer = DeepBase100,
    onSecondaryContainer = DeepBase900,

    tertiary = DeepBrand900,
    onTertiary = DeepBase0,
    tertiaryContainer = DeepBrand100,
    onTertiaryContainer = DeepBrand1000,

    error = DeepRed500,
    onError = DeepBase0,
    errorContainer = DeepRed200,
    onErrorContainer = DeepRed600,

    background = DeepBrand1000,
    onBackground = DeepBase0,
    surface = DeepBase0,
    onSurface = DeepBase1000,
    surfaceVariant = DeepBase100,
    onSurfaceVariant = DeepBase900,

    outline = DeepBase1000Alpha8,
    outlineVariant = DeepBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = DeepBrand500,
    onPrimary = DeepBrand1000,
    primaryContainer = DeepBrand900,
    onPrimaryContainer = DeepBrand500,

    secondary = DeepBase400,
    onSecondary = DeepBase1000,
    secondaryContainer = DeepBase900,
    onSecondaryContainer = DeepBase150,

    tertiary = DeepBrand500,
    onTertiary = DeepBase1000,
    tertiaryContainer = DeepBrand900,
    onTertiaryContainer = DeepBrand500,

    error = DeepRed500,
    onError = DeepBase0,
    errorContainer = DeepRed600,
    onErrorContainer = DeepRed200,

    background = DeepBase1000,
    onBackground = DeepBase0,
    surface = DeepBase950,
    onSurface = DeepBase0,
    surfaceVariant = DeepBase900,
    onSurfaceVariant = DeepBase150,

    outline = DeepBase100Alpha10,
    outlineVariant = DeepBase800,
)

@Immutable
data class ExtendedColorScheme(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColorScheme = ExtendedColorScheme(
    primaryHover = DeepBrand600,
    destructiveHover = DeepRed600,
    destructiveSecondaryOutline = DeepRed200,
    disabledOutline = DeepBase200,
    disabledFill = DeepBase150,
    successOutline = DeepBrand100,
    success = DeepBrand600,
    onSuccess = DeepBase0,
    secondaryFill = DeepBase100,

    textPrimary = DeepBase1000,
    textTertiary = DeepBase800,
    textSecondary = DeepBase900,
    textPlaceholder = DeepBase700,
    textDisabled = DeepBase400,

    surfaceLower = DeepBase100,
    surfaceHigher = DeepBase100,
    surfaceOutline = DeepBase1000Alpha14,
    overlay = DeepBase1000Alpha80,

    accentBlue = DeepBlue,
    accentPurple = DeepPurple,
    accentViolet = DeepViolet,
    accentPink = DeepPink,
    accentOrange = DeepOrange,
    accentYellow = DeepYellow,
    accentGreen = DeepGreen,
    accentTeal = DeepTeal,
    accentLightBlue = DeepLightBlue,
    accentGrey = DeepGrey,

    cakeViolet = DeepCakeLightViolet,
    cakeGreen = DeepCakeLightGreen,
    cakeBlue = DeepCakeLightBlue,
    cakePink = DeepCakeLightPink,
    cakeOrange = DeepCakeLightOrange,
    cakeYellow = DeepCakeLightYellow,
    cakeTeal = DeepCakeLightTeal,
    cakePurple = DeepCakeLightPurple,
    cakeRed = DeepCakeLightRed,
    cakeMint = DeepCakeLightMint,
)

val DarkExtendedColorScheme = ExtendedColorScheme(
    primaryHover = DeepBrand600,
    destructiveHover = DeepRed600,
    destructiveSecondaryOutline = DeepRed200,
    disabledOutline = DeepBase900,
    disabledFill = DeepBase1000,
    successOutline = DeepBrand500Alpha40,
    success = DeepBrand500,
    onSuccess = DeepBase1000,
    secondaryFill = DeepBase900,

    textPrimary = DeepBase0,
    textTertiary = DeepBase200,
    textSecondary = DeepBase150,
    textPlaceholder = DeepBase400,
    textDisabled = DeepBase500,

    surfaceLower = DeepBase1000,
    surfaceHigher = DeepBase900,
    surfaceOutline = DeepBase100Alpha10Alt,
    overlay = DeepBase1000Alpha80,

    accentBlue = DeepBlue,
    accentPurple = DeepPurple,
    accentViolet = DeepViolet,
    accentPink = DeepPink,
    accentOrange = DeepOrange,
    accentYellow = DeepYellow,
    accentGreen = DeepGreen,
    accentTeal = DeepTeal,
    accentLightBlue = DeepLightBlue,
    accentGrey = DeepGrey,

    cakeViolet = DeepCakeDarkViolet,
    cakeGreen = DeepCakeDarkGreen,
    cakeBlue = DeepCakeDarkBlue,
    cakePink = DeepCakeDarkPink,
    cakeOrange = DeepCakeDarkOrange,
    cakeYellow = DeepCakeDarkYellow,
    cakeTeal = DeepCakeDarkTeal,
    cakePurple = DeepCakeDarkPurple,
    cakeRed = DeepCakeDarkRed,
    cakeMint = DeepCakeDarkMint,
)

val LocalExtendedColorScheme = staticCompositionLocalOf { LightExtendedColorScheme }
val ColorScheme.extended: ExtendedColorScheme
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColorScheme.current
