package br.com.payflowapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class Modalidade { MENSAL, ANUAL }

enum class CategoriaAssinatura(val label: String) {
    STREAMING("Streaming"),
    MUSICA("Música"),
    JOGOS("Jogos"),
    PRODUTIVIDADE("Produtividade"),
    EDUCACAO("Educação"),
    SAUDE("Saúde"),
    FINANCAS("Finanças"),
    OUTROS("Outros")
}

@Entity(tableName = "assinaturas")
data class Assinatura(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nomeServico: String,
    val valor: Double,
    val modalidade: Modalidade,
    val diaVencimento: Int,          // 1..31
    val categoria: CategoriaAssinatura,
    val urlServico: String? = "",
    val ativa: Boolean = true,

    // Campos adicionados para o histórico
    val dataInicio: LocalDate? = null,
    val dataFim: LocalDate? = null
)
