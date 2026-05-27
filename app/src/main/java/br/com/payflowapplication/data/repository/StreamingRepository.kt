package br.com.payflowapplication.data.repository

import br.com.payflowapplication.data.remote.StreamingApiService
import br.com.payflowapplication.model.ConsumoMensal
import br.com.payflowapplication.model.Streaming
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fallback returned when the API call fails or network is unavailable.
 * Uses diasUtilizados == totalDiasNoMes so usageScore = 1.0 (neutral/unknown),
 * preventing false "pouco usada" flags when data is simply unavailable.
 */
private val UNKNOWN_USAGE_FALLBACK = ConsumoMensal(
    id = null,
    username = "",
    mesReferencia = "",
    totalDiasNoMes = 30,
    diasUtilizados = 30,  // 100% → score=1.0 → never flagged as "pouco usada"
    totalMinutosMes = 0,
)

@Singleton
class StreamingRepository @Inject constructor(
    private val api: StreamingApiService
) {
    /**
     * Search streamings by name (case-insensitive contains).
     * Returns empty list on error to avoid crashing the UI.
     */
    suspend fun search(query: String): List<Streaming> {
        if (query.isBlank()) return emptyList()
        return try {
            api.searchStreamings(query.trim().lowercase())
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Fetches monthly consumption for [nomeServico] in [anomes] (YYYY-MM format).
     * Returns a low-usage fallback on any network / parse error so the UI never
     * crashes — unknown services also get the fallback from the mock server itself.
     *
     * @param username   User identifier (defaults to "default").
     * @param nomeServico Service name, e.g. "Netflix".
     * @param anomes     Month reference in YYYY-MM format, e.g. "2026-05".
     */
    suspend fun getConsumoMensal(
        nomeServico: String,
        anomes: String,
        username: String = "default",
    ): ConsumoMensal = try {
        api.getConsumoMensal(
            username = username,
            nome     = nomeServico.trim().lowercase(),
            anomes   = anomes,
        )
    } catch (e: Exception) {
        UNKNOWN_USAGE_FALLBACK
    }
}
