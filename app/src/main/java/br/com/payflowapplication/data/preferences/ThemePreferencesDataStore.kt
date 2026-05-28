package br.com.payflowapplication.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extensão de nível de arquivo — cria uma única instância do DataStore por Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "payflow_preferences"
)

/**
 * Repositório de preferências persistentes do usuário.
 *
 * Usa [DataStore<Preferences>] para sobreviver ao fechamento do app.
 * A chave [DARK_MODE_KEY] armazena se o tema escuro está ativado.
 * Valor padrão: **true** (dark theme).
 */
@Singleton
class ThemePreferencesDataStore @Inject constructor(
    private val context: Context,
) {
    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
    }

    /** Flow que emite o valor atual do tema. Nunca fecha enquanto o app estiver ativo. */
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: true } // padrão: dark

    /** Persiste a preferência de tema. Suspending — deve ser chamado de uma coroutine. */
    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }
}

