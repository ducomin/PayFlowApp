package br.com.payflowapplication.navigation

object Routes {
    // Home / Dashboard
    const val HOME = "home"

    // Histórico
    const val HISTORICO = "historico"

    // Cadastro / Edição de Assinatura
    const val CADASTRO_ASSINATURA = "cadastro_assinatura"
    const val CADASTRO_ASSINATURA_EDIT = "cadastro_assinatura/{assinaturaId}"
    fun editRoute(id: Long) = "cadastro_assinatura/$id"

    // Perfil
    const val PERFIL = "perfil"

    //Detalhe
    const val DETALHE_ASSINATURA = "detalhe_assinatura/{assinaturaId}"
    fun detalheRoute(id: Long) = "detalhe_assinatura/$id"
}
