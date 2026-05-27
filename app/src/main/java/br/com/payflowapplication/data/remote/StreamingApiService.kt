package br.com.payflowapplication.data.remote

import br.com.payflowapplication.model.ConsumoMensal
import br.com.payflowapplication.model.Pagamento
import br.com.payflowapplication.model.Streaming
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StreamingApiService {

    /**
     * GET /api/v1/streamings/search?nome=<query>
     * Handled by our custom server.js — performs case-insensitive contains filter.
     * Empty query returns all streamings.
     */
    @GET("api/v1/streamings/search")
    suspend fun searchStreamings(
        @Query("nome") nome: String
    ): List<Streaming>

    /**
     * GET /api/v1/streamings/{username}/consumo_mensal?nome={nome}&anomes={YYYY-MM}
     *
     * Returns the real monthly usage for a known service, or a low-usage fallback
     * (dias_utilizados = 3) for services not present in the mock DB.
     */
    @GET("api/v1/streamings/{username}/consumo_mensal")
    suspend fun getConsumoMensal(
        @Path("username")       username: String,
        @Query("nome")          nome: String,
        @Query("anomes")        anomes: String,
    ): ConsumoMensal

    /**
     * GET /api/v1/streamings/pagamentos?nome={nome}
     * Returns the payment history for a service.
     */
    @GET("api/v1/streamings/pagamentos")
    suspend fun getPagamentos(
        @Query("nomeservico") nomeservico: String
    ): List<Pagamento>
}
