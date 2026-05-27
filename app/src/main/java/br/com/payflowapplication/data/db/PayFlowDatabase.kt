package br.com.payflowapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.payflowapplication.model.Assinatura
import br.com.payflowapplication.model.Notificacao

@Database(entities = [Assinatura::class, Notificacao::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PayFlowDatabase : RoomDatabase() {
    abstract fun assinaturaDao(): AssinaturaDao
    abstract fun historyDao(): HistoryDao
    abstract fun notificacaoDao(): NotificacaoDao
}
