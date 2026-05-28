package br.com.payflowapplication.data.repository

import br.com.payflowapplication.data.db.HistoryDao
import br.com.payflowapplication.model.Assinatura

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao
) {
    fun getHistorico(): Flow<List<Assinatura>> {
        return historyDao.getHistoricoAssinaturas()
    }
}
