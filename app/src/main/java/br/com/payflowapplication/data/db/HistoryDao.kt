package br.com.payflowapplication.data.db

import androidx.room.Dao
import androidx.room.Query
import br.com.payflowapplication.model.Assinatura
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM assinaturas WHERE ativa = 0 ORDER BY dataFim DESC")
    fun getHistoricoAssinaturas(): Flow<List<Assinatura>>

}
