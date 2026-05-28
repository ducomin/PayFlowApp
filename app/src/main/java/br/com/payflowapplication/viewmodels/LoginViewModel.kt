package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.preferences.AuthPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authPreferences: AuthPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun login() {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Preencha todos os campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Simulação de autenticação básica
            kotlinx.coroutines.delay(1000)
            if (state.username == "admin" && state.password == "1234") {
                // Geração de token JWT simulado (header.payload.signature)
                val dummyToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                        "eyJzdWIiOiJ3aXRpa2F3YSIsIm5hbWUiOiJXaXRpa2F3YSIsImlhdCI6MTUxNjIzOTAyMn0." +
                        "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                
                authPreferences.saveAuthData(dummyToken, state.username)

                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Usuário ou senha inválidos") }
            }
        }
    }
    
    fun resetState() {
        _uiState.update { LoginUiState() }
    }
}
