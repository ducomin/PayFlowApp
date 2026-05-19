package br.com.payflowapplication.navigation

object Routes {
    const val CADASTRO_ASSINATURA = "cadastro_assinatura"
    const val CADASTRO_ASSINATURA_EDIT = "cadastro_assinatura/{assinaturaId}"
    fun editRoute(id: Long) = "cadastro_assinatura/$id"
}

