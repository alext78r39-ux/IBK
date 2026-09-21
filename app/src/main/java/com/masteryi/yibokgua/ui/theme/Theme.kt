package com.masteryi.yibokgua.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.masteryi.yibokgua.settings.ColorPreset
import com.masteryi.yibokgua.settings.ThemeMode

// ==================== 朱砂金（預設） ====================
private val LightColorScheme = lightColorScheme(
    primary = YiRedLight, onPrimary = Color.White,
    primaryContainer = YiRedContainer, onPrimaryContainer = OnYiRedContainer,
    secondary = YiGoldLight, onSecondary = Color.White,
    secondaryContainer = YiGoldContainer, onSecondaryContainer = OnYiGoldContainer,
    tertiary = YiGoldLight,
    background = YiPaper, onBackground = YiOnPaper,
    surface = YiPaper, onSurface = YiOnPaper,
    surfaceVariant = YiSurfaceVariantLight, onSurfaceVariant = YiOnSurfaceVariantLight,
    outline = YiOutlineLight,
    error = Color(0xFFB3261E), onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = YiRedDark, onPrimary = OnYiRedDark,
    primaryContainer = YiDarkRedContainer, onPrimaryContainer = YiRedContainer,
    secondary = YiGoldDark, onSecondary = OnYiGoldDark,
    secondaryContainer = YiGoldContainerDark, onSecondaryContainer = YiGoldContainer,
    tertiary = YiGoldDark,
    background = YiPaperDark, onBackground = Color(0xFFF0EDE6),
    surface = YiPaperDark, onSurface = Color(0xFFF0EDE6),
    surfaceVariant = YiSurfaceVariantDark, onSurfaceVariant = YiOnSurfaceVariantDark,
    outline = YiOutlineDark,
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005)
)

// ==================== 青瓷藍 ====================
private val BlueLightScheme = lightColorScheme(
    primary = BluePrimaryLight, onPrimary = Color.White,
    primaryContainer = BluePrimaryContainerLight, onPrimaryContainer = OnBluePrimaryContainerLight,
    secondary = BlueSecondaryLight, onSecondary = Color.White,
    secondaryContainer = BlueSecondaryContainerLight, onSecondaryContainer = OnBlueSecondaryContainerLight,
    tertiary = BlueSecondaryLight,
    background = YiPaper, onBackground = YiOnPaper,
    surface = YiPaper, onSurface = YiOnPaper,
    surfaceVariant = YiSurfaceVariantLight, onSurfaceVariant = YiOnSurfaceVariantLight,
    outline = YiOutlineLight,
    error = Color(0xFFB3261E), onError = Color.White
)

private val BlueDarkScheme = darkColorScheme(
    primary = BluePrimaryDark, onPrimary = OnBluePrimaryDark,
    primaryContainer = BlueDarkPrimaryContainerLight, onPrimaryContainer = BluePrimaryContainerLight,
    secondary = BlueSecondaryDark, onSecondary = OnBlueSecondaryDark,
    secondaryContainer = BlueSecondaryContainerDark, onSecondaryContainer = BlueSecondaryContainerLight,
    tertiary = BlueSecondaryDark,
    background = YiPaperDark, onBackground = Color(0xFFF0EDE6),
    surface = YiPaperDark, onSurface = Color(0xFFF0EDE6),
    surfaceVariant = YiSurfaceVariantDark, onSurfaceVariant = YiOnSurfaceVariantDark,
    outline = YiOutlineDark,
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005)
)

// ==================== 翡翠綠 ====================
private val GreenLightScheme = lightColorScheme(
    primary = GreenPrimaryLight, onPrimary = Color.White,
    primaryContainer = GreenPrimaryContainerLight, onPrimaryContainer = OnGreenPrimaryContainerLight,
    secondary = GreenSecondaryLight, onSecondary = Color.White,
    secondaryContainer = GreenSecondaryContainerLight, onSecondaryContainer = OnGreenSecondaryContainerLight,
    tertiary = GreenSecondaryLight,
    background = YiPaper, onBackground = YiOnPaper,
    surface = YiPaper, onSurface = YiOnPaper,
    surfaceVariant = YiSurfaceVariantLight, onSurfaceVariant = YiOnSurfaceVariantLight,
    outline = YiOutlineLight,
    error = Color(0xFFB3261E), onError = Color.White
)

private val GreenDarkScheme = darkColorScheme(
    primary = GreenPrimaryDark, onPrimary = OnGreenPrimaryDark,
    primaryContainer = GreenDarkPrimaryContainerLight, onPrimaryContainer = GreenPrimaryContainerLight,
    secondary = GreenSecondaryDark, onSecondary = OnGreenSecondaryDark,
    secondaryContainer = GreenSecondaryContainerDark, onSecondaryContainer = GreenSecondaryContainerLight,
    tertiary = GreenSecondaryDark,
    background = YiPaperDark, onBackground = Color(0xFFF0EDE6),
    surface = YiPaperDark, onSurface = Color(0xFFF0EDE6),
    surfaceVariant = YiSurfaceVariantDark, onSurfaceVariant = YiOnSurfaceVariantDark,
    outline = YiOutlineDark,
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005)
)

// ==================== 紫水晶 ====================
private val PurpleLightScheme = lightColorScheme(
    primary = PurplePrimaryLight, onPrimary = Color.White,
    primaryContainer = PurplePrimaryContainerLight, onPrimaryContainer = OnPurplePrimaryContainerLight,
    secondary = PurpleSecondaryLight, onSecondary = Color.White,
    secondaryContainer = PurpleSecondaryContainerLight, onSecondaryContainer = OnPurpleSecondaryContainerLight,
    tertiary = PurpleSecondaryLight,
    background = YiPaper, onBackground = YiOnPaper,
    surface = YiPaper, onSurface = YiOnPaper,
    surfaceVariant = YiSurfaceVariantLight, onSurfaceVariant = YiOnSurfaceVariantLight,
    outline = YiOutlineLight,
    error = Color(0xFFB3261E), onError = Color.White
)

private val PurpleDarkScheme = darkColorScheme(
    primary = PurplePrimaryDark, onPrimary = OnPurplePrimaryDark,
    primaryContainer = PurpleDarkPrimaryContainerLight, onPrimaryContainer = PurplePrimaryContainerLight,
    secondary = PurpleSecondaryDark, onSecondary = OnPurpleSecondaryDark,
    secondaryContainer = PurpleSecondaryContainerDark, onSecondaryContainer = PurpleSecondaryContainerLight,
    tertiary = PurpleSecondaryDark,
    background = YiPaperDark, onBackground = Color(0xFFF0EDE6),
    surface = YiPaperDark, onSurface = Color(0xFFF0EDE6),
    surfaceVariant = YiSurfaceVariantDark, onSurfaceVariant = YiOnSurfaceVariantDark,
    outline = YiOutlineDark,
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005)
)

// ==================== 琥珀橘 ====================
private val OrangeLightScheme = lightColorScheme(
    primary = OrangePrimaryLight, onPrimary = Color.White,
    primaryContainer = OrangePrimaryContainerLight, onPrimaryContainer = OnOrangePrimaryContainerLight,
    secondary = OrangeSecondaryLight, onSecondary = Color.White,
    secondaryContainer = OrangeSecondaryContainerLight, onSecondaryContainer = OnOrangeSecondaryContainerLight,
    tertiary = OrangeSecondaryLight,
    background = YiPaper, onBackground = YiOnPaper,
    surface = YiPaper, onSurface = YiOnPaper,
    surfaceVariant = YiSurfaceVariantLight, onSurfaceVariant = YiOnSurfaceVariantLight,
    outline = YiOutlineLight,
    error = Color(0xFFB3261E), onError = Color.White
)

private val OrangeDarkScheme = darkColorScheme(
    primary = OrangePrimaryDark, onPrimary = OnOrangePrimaryDark,
    primaryContainer = OrangeDarkPrimaryContainerLight, onPrimaryContainer = OrangePrimaryContainerLight,
    secondary = OrangeSecondaryDark, onSecondary = OnOrangeSecondaryDark,
    secondaryContainer = OrangeSecondaryContainerDark, onSecondaryContainer = OrangeSecondaryContainerLight,
    tertiary = OrangeSecondaryDark,
    background = YiPaperDark, onBackground = Color(0xFFF0EDE6),
    surface = YiPaperDark, onSurface = Color(0xFFF0EDE6),
    surfaceVariant = YiSurfaceVariantDark, onSurfaceVariant = YiOnSurfaceVariantDark,
    outline = YiOutlineDark,
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005)
)

private fun colorSchemeFor(preset: ColorPreset, dark: Boolean): androidx.compose.material3.ColorScheme = when (preset) {
    ColorPreset.DEFAULT -> if (dark) DarkColorScheme else LightColorScheme
    ColorPreset.BLUE -> if (dark) BlueDarkScheme else BlueLightScheme
    ColorPreset.GREEN -> if (dark) GreenDarkScheme else GreenLightScheme
    ColorPreset.PURPLE -> if (dark) PurpleDarkScheme else PurpleLightScheme
    ColorPreset.ORANGE -> if (dark) OrangeDarkScheme else OrangeLightScheme
}

@Composable
fun YibokguaTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    fontSizeScale: Float = 1.0f,
    colorPreset: ColorPreset = ColorPreset.DEFAULT,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        else -> colorSchemeFor(colorPreset, darkTheme)
    }
    val scaledTypography = scaledTypography(fontSizeScale)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}

/** 依倍率縮放所有字級 */
private fun scaledTypography(scale: Float) = androidx.compose.material3.Typography(
    displayLarge = Typography.displayLarge.copy(fontSize = Typography.displayLarge.fontSize * scale),
    displayMedium = Typography.displayMedium.copy(fontSize = Typography.displayMedium.fontSize * scale),
    displaySmall = Typography.displaySmall.copy(fontSize = Typography.displaySmall.fontSize * scale),
    headlineLarge = Typography.headlineLarge.copy(fontSize = Typography.headlineLarge.fontSize * scale),
    headlineMedium = Typography.headlineMedium.copy(fontSize = Typography.headlineMedium.fontSize * scale),
    headlineSmall = Typography.headlineSmall.copy(fontSize = Typography.headlineSmall.fontSize * scale),
    titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * scale),
    titleMedium = Typography.titleMedium.copy(fontSize = Typography.titleMedium.fontSize * scale),
    titleSmall = Typography.titleSmall.copy(fontSize = Typography.titleSmall.fontSize * scale),
    bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * scale),
    bodyMedium = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * scale),
    bodySmall = Typography.bodySmall.copy(fontSize = Typography.bodySmall.fontSize * scale),
    labelLarge = Typography.labelLarge.copy(fontSize = Typography.labelLarge.fontSize * scale),
    labelMedium = Typography.labelMedium.copy(fontSize = Typography.labelMedium.fontSize * scale),
    labelSmall = Typography.labelSmall.copy(fontSize = Typography.labelSmall.fontSize * scale)
)
