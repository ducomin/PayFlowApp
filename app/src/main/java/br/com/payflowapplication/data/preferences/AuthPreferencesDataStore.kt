package br.com.payflowapplication.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_preferences"
)

@Singleton
class AuthPreferencesDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    val authToken: Flow<String?> = context.authDataStore.data
        .map { prefs -> prefs[AUTH_TOKEN_KEY] }

    val username: Flow<String?> = context.authDataStore.data
        .map { prefs -> prefs[USERNAME_KEY] }

    suspend fun saveAuthData(token: String, username: String) {
        context.authDataStore.edit { prefs ->
            prefs[AUTH_TOKEN_KEY] = token
            prefs[USERNAME_KEY] = username
        }
    }

    suspend fun saveToken(token: String) {
        context.authDataStore.edit { prefs ->
            prefs[AUTH_TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        context.authDataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN_KEY)
            prefs.remove(USERNAME_KEY)
        }
    }
}
