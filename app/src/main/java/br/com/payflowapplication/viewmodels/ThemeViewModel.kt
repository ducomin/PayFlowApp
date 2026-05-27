package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.preferences.ThemePreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel global de tema — vive no escopo da Activity (MainActivity).
 *
 * - Lê a preferência salva no [ThemePreferencesDataStore] via Flow.
 * - [isDarkTheme] é um [StateFlow] observável por toda a árvore Compose.
 * - [setDarkTheme] persiste a mudança no DataStore para sobreviver ao app fechar.
 *
 * O valor inicial é **true** (dark) enquanto o DataStore carrega do disco.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferencesDataStore,
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = themePreferences.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly, // lê do disco imediatamente ao criar o VM
            initialValue = true,              // default dark enquanto carrega
        )

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkTheme(enabled)
        }
    }
}

