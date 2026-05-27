package br.com.payflowapplication.data.repository

import br.com.payflowapplication.data.db.NotificacaoDao
import br.com.payflowapplication.model.Notificacao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificacaoRepository @Inject constructor(
    private val dao: NotificacaoDao
) {
    /** Observa lista de notificações de um usuário em tempo real. */
    fun observar(username: String): Flow<List<Notificacao>> =
        dao.observarPorUsuario(username)

    /** Observa contador de não lidas para badge na NavBar. */
    fun contarNaoLidas(username: String): Flow<Int> =
        dao.contarNaoLidas(username)

    /** Marca todas as notificações do usuário como lidas. */
    suspend fun marcarTodasComoLidas(username: String) =
        dao.marcarTodasComoLidas(username)

    /** Marca uma notificação específica como lida. */
    suspend fun marcarComoLida(id: Long) =
        dao.marcarComoLida(id)

    /** Insere lista de notificações (seed inicial ou sync com API). */
    suspend fun inserirTodas(lista: List<Notificacao>) =
        dao.inserirTodas(lista)

    /** Deleta todas as notificações do usuário (reset). */
    suspend fun deletarPorUsuario(username: String) =
        dao.deletarPorUsuario(username)
}

