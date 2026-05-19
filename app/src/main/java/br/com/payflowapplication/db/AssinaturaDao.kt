package br.com.payflowapplication.db

import androidx.room.*
import br.com.payflowapplication.model.Assinatura
import kotlinx.coroutines.flow.Flow

@Dao
interface AssinaturaDao {

    @Query("SELECT * FROM assinaturas ORDER BY id DESC")
    fun getAll(): Flow<List<Assinatura>>

    @Query("SELECT * FROM assinaturas WHERE id = :id")
    suspend fun getById(id: Long): Assinatura?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assinatura: Assinatura): Long

    @Update
    suspend fun update(assinatura: Assinatura)

    @Delete
    suspend fun delete(assinatura: Assinatura)
}

