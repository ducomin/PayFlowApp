package br.com.payflowapplication.data.db

import androidx.room.*
import br.com.payflowapplication.model.Assinatura
import kotlinx.coroutines.flow.Flow

@Dao
interface AssinaturaDao {

    @Query("SELECT * FROM assinaturas ORDER BY id DESC")
    fun getAll(): Flow<List<Assinatura>>

    @Query("SELECT * FROM assinaturas WHERE ativa = 1 ORDER BY diaVencimento ASC")
    fun getAtivas(): Flow<List<Assinatura>>

    @Query("SELECT * FROM assinaturas WHERE id = :id")
    suspend fun getById(id: Long): Assinatura?

    /**
     * Checks for a duplicate name (case-insensitive).
     * [excludeId] should be the current record id in edit mode (pass 0L for new records).
     * Returns null when no duplicate exists.
     */
    @Query(
        "SELECT * FROM assinaturas " +
        "WHERE LOWER(nomeServico) = LOWER(:nome) " +
        "AND id != :excludeId " +
        "LIMIT 1"
    )
    suspend fun findByNome(nome: String, excludeId: Long): Assinatura?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assinatura: Assinatura): Long

    @Update
    suspend fun update(assinatura: Assinatura)

    @Delete
    suspend fun delete(assinatura: Assinatura)
}
