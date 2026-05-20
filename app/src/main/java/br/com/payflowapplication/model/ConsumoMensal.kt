package br.com.payflowapplication.model

import com.google.gson.annotations.SerializedName

/**
 * DTO returned by:
 * GET /api/v1/streamings/{username}/consumo_mensal?nome={nome}&anomes={YYYY-MM}
 */
data class ConsumoMensal(
    @SerializedName("id")                val id: String?,
    @SerializedName("username")          val username: String,
    @SerializedName("mes_referencia")    val mesReferencia: String,
    @SerializedName("total_dias_no_mes") val totalDiasNoMes: Int,
    @SerializedName("dias_utilizados")   val diasUtilizados: Int,
    @SerializedName("total_minutos_mes") val totalMinutosMes: Int,
) {
    /** Usage score 0..1 — fraction of days actually used in the month. */
    val usageScore: Float
        get() = if (totalDiasNoMes > 0) diasUtilizados.toFloat() / totalDiasNoMes else 0f
}

