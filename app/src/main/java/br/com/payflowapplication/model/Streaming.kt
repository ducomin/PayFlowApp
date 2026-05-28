package br.com.payflowapplication.model

import com.google.gson.annotations.SerializedName

data class Streaming(
    @SerializedName("id")           val id: String,
    @SerializedName("nome")         val nome: String,
    @SerializedName("logo_url")     val logoUrl: String,
    @SerializedName("categoria_principal") val categoriaPrincipal: String
)

