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
import com.masteryi.yibokgua.settings.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = YiRedLight,
    onPrimary = Color.White,
    primaryContainer = YiRedContainer,
    onPrimaryContainer = OnYiRedContainer,
    secondary = YiGoldLight,
    onSecondary = Color.White,
    secondaryContainer = YiGoldContainer,
    onSecondaryContainer = OnYiGoldContainer,
    tertiary = YiGoldLight,
    background = YiPaper,
    onBackground = YiOnPaper,
    surface = YiPaper,
    onSurface = YiOnPaper,
    surfaceVariant = YiSurfaceVariantLight,
    onSurfaceVariant = YiOnSurfaceVariantLight,
    outline = YiOutlineLight,
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = YiRedDark,
    onPrimary = OnYiRedDark,
    primaryContainer = YiDarkRedContainer,
    onPrimaryContainer = YiRedContainer,
    secondary = YiGoldDark,
    onSecondary = OnYiGoldDark,
    secondaryContainer = YiGoldContainerDark,
    onSecondaryContainer = YiGoldContainer,
    tertiary = YiGoldDark,
    background = YiPaperDark,
    onBackground = Color(0xFFF0EDE6),
    surface = YiPaperDark,
    onSurface = Color(0xFFF0EDE6),
    surfaceVariant = YiSurfaceVariantDark,
    onSurfaceVariant = YiOnSurfaceVariantDark,
    outline = YiOutlineDark,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun YibokguaTheme(
    // 主題模式：SYSTEM 跟隨系統、LIGHT 淺色、DARK 深色
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    // 使用 Android 12+ 動態取色；需自訂「朱砂紅/玄金」色系時設為 false
    dynamicColor: Boolean = false,
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
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
