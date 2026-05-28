package br.com.payflowapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.payflowapplication.data.repository.AssinaturaRepository
import br.com.payflowapplication.model.Assinatura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── UI State ─────────────────────────────────────────────────────────────────

sealed interface ProfileUiState {

    data object Loading : ProfileUiState

    data class Error(
        val message: String
    ) : ProfileUiState

    data class Success(
        val nomeUsuario: String,
        val email: String,
        val planoPremium: Boolean,
        val totalAtivas: Int,
        val totalCategorias: Int,
        val totalMensal: Double,
        val assinaturas: List<Assinatura>,
    ) : ProfileUiState
}

// ─── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val assinaturaRepository: AssinaturaRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        carregarPerfil()
    }

    // ─── Load profile ────────────────────────────────────────────────────────

    private fun carregarPerfil() {

        viewModelScope.launch {

            assinaturaRepository.getAtivas()
                .catch { e ->
                    _uiState.value = ProfileUiState.Error(
                        e.message ?: "Erro ao carregar perfil"
                    )
                }
                .collectLatest { assinaturas ->

                    val totalMensal = assinaturas.sumOf { it.valor }
                    val categorias = assinaturas.map { it.categoria }.distinct()

                    _uiState.value = ProfileUiState.Success(
                        nomeUsuario = "Usuário Silva",
                        email = "usuario@email.com",
                        planoPremium = true,
                        totalAtivas = assinaturas.size,
                        totalCategorias = categorias.size,
                        totalMensal = totalMensal,
                        assinaturas = assinaturas
                    )
                }
        }
    }
}