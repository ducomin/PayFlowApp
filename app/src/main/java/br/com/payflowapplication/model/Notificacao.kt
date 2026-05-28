package br.com.payflowapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/** Tipos mínimos de notificação conforme regra de negócio HU-NOTIF */
enum class TipoNotificacao {
    VENCIMENTO,     // alerta crítico — destaque semântico de erro/warning
    BAIXO_USO,      // aviso informativo — destaque secondary
    PROMOCAO,       // informativo — destaque tertiary
    RENOVACAO       // confirmação — destaque success
}

@Entity(tableName = "notificacoes")
data class Notificacao(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val tipo: TipoNotificacao,
    val lida: Boolean = false,
    val criadaEm: LocalDateTime = LocalDateTime.now(),
    /** ID da assinatura relacionada, se houver navegação */
    val assinaturaId: Long? = null,
    val username: String = "user1"
)

