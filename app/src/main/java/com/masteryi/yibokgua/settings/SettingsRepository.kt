package com.masteryi.yibokgua.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode(val storeValue: Int) {
    SYSTEM(0),
    LIGHT(1),
    DARK(2)
}

enum class ColorPreset(val label: String, val storeValue: Int) {
    DEFAULT("朱砂金", 0),
    BLUE("青瓷藍", 1),
    GREEN("翡翠綠", 2),
    PURPLE("紫水晶", 3),
    ORANGE("琥珀橘", 4)
}

private val Context.settingsDataStore by preferencesDataStore(name = "yibokgua_settings")

data class Settings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = false,
    val fontSizeScale: Float = 1.0f,
    val borderWidth: Float = 0.5f,
    val keyboardScale: Float = 1.0f,
    val colorPreset: ColorPreset = ColorPreset.DEFAULT
)

class SettingsRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = intPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val FONT_SIZE_SCALE = floatPreferencesKey("font_size_scale")
        val BORDER_WIDTH = floatPreferencesKey("border_width")
        val KEYBOARD_SCALE = floatPreferencesKey("keyboard_scale")
        val COLOR_PRESET = intPreferencesKey("color_preset")
    }

    val settings: Flow<Settings> = context.settingsDataStore.data.map { prefs ->
        Settings(
            themeMode = ThemeMode.entries.firstOrNull {
                it.storeValue == (prefs[Keys.THEME_MODE] ?: ThemeMode.SYSTEM.storeValue)
            } ?: ThemeMode.SYSTEM,
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: false,
            fontSizeScale = prefs[Keys.FONT_SIZE_SCALE] ?: 1.0f,
            borderWidth = prefs[Keys.BORDER_WIDTH] ?: 0.5f,
            keyboardScale = prefs[Keys.KEYBOARD_SCALE] ?: 1.0f,
            colorPreset = ColorPreset.entries.firstOrNull {
                it.storeValue == (prefs[Keys.COLOR_PRESET] ?: ColorPreset.DEFAULT.storeValue)
            } ?: ColorPreset.DEFAULT
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode.storeValue
        }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.DYNAMIC_COLOR] = enabled
        }
    }

    suspend fun setFontSizeScale(scale: Float) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.FONT_SIZE_SCALE] = scale
        }
    }

    suspend fun setBorderWidth(width: Float) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.BORDER_WIDTH] = width
        }
    }

    suspend fun setKeyboardScale(scale: Float) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.KEYBOARD_SCALE] = scale
        }
    }

    suspend fun setColorPreset(preset: ColorPreset) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.COLOR_PRESET] = preset.storeValue
        }
    }

    suspend fun reset() {
        context.settingsDataStore.edit { it.clear() }
    }
}
