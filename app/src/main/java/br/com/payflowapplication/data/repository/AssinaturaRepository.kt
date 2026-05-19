package br.com.payflowapplication.data.repository

import br.com.payflowapplication.data.db.AssinaturaDao
import br.com.payflowapplication.model.Assinatura
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssinaturaRepository @Inject constructor(
    private val dao: AssinaturaDao
) {
    fun getAll(): Flow<List<Assinatura>> = dao.getAll()

    suspend fun getById(id: Long): Assinatura? = dao.getById(id)

    suspend fun save(assinatura: Assinatura): Long =
        if (assinatura.id == 0L) dao.insert(assinatura)
        else { dao.update(assinatura); assinatura.id }

    suspend fun delete(assinatura: Assinatura) = dao.delete(assinatura)
}

