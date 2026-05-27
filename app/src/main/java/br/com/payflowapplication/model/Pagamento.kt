package br.com.payflowapplication.model

import com.google.gson.annotations.SerializedName

data class Pagamento(
    @SerializedName("nomeservico") val nomeservico: String,
    @SerializedName("plano") val plano: String,
    @SerializedName("data_cobranca") val dataCobranca: String,
    @SerializedName("status") val status: String,
    @SerializedName("valor") val valor: Double,
    @SerializedName("metodo_pagamento") val metodoPagamento: String
)
