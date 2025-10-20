package com.example.aps.DarkTheme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Extensão para DataStore para acesso fácil. A falta desta importação no ProfileScreen causa erros!
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel : ViewModel() {

    private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")

    /**
     * Observa o DataStore e emite o estado atual do Modo Escuro.
     */
    fun getDarkModeState(context: Context): StateFlow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[IS_DARK_MODE] ?: false // Padrão: Modo Claro
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
    }

    /**
     * Altera e salva o estado do modo escuro no DataStore.
     */
    fun setDarkMode(context: Context, isDark: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { settings ->
                settings[IS_DARK_MODE] = isDark
            }
        }
    }
}