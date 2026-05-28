package br.com.payflowapplication.data.db

import androidx.room.*
import br.com.payflowapplication.model.Notificacao
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificacaoDao {

    @Query("SELECT * FROM notificacoes WHERE username = :username ORDER BY criadaEm DESC")
    fun observarPorUsuario(username: String): Flow<List<Notificacao>>

    @Query("SELECT COUNT(*) FROM notificacoes WHERE username = :username AND lida = 0")
    fun contarNaoLidas(username: String): Flow<Int>

    @Query("UPDATE notificacoes SET lida = 1 WHERE username = :username")
    suspend fun marcarTodasComoLidas(username: String)

    @Query("UPDATE notificacoes SET lida = 1 WHERE id = :id")
    suspend fun marcarComoLida(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(notificacao: Notificacao): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTodas(lista: List<Notificacao>)

    @Query("DELETE FROM notificacoes WHERE username = :username")
    suspend fun deletarPorUsuario(username: String)
}

