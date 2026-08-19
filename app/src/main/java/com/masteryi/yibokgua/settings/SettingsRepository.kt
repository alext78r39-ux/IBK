package com.masteryi.yibokgua.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode(val storeValue: Int) {
    SYSTEM(0),
    LIGHT(1),
    DARK(2)
}

private val Context.settingsDataStore by preferencesDataStore(name = "yibokgua_settings")

data class Settings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = false
)

class SettingsRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = intPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    }

    val settings: Flow<Settings> = context.settingsDataStore.data.map { prefs ->
        Settings(
            themeMode = ThemeMode.entries.firstOrNull {
                it.storeValue == (prefs[Keys.THEME_MODE] ?: ThemeMode.SYSTEM.storeValue)
            } ?: ThemeMode.SYSTEM,
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: false
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

    suspend fun reset() {
        context.settingsDataStore.edit { it.clear() }
    }
}
