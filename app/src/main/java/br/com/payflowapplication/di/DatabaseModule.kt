package br.com.payflowapplication.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.payflowapplication.data.db.AssinaturaDao
import br.com.payflowapplication.data.db.HistoryDao
import br.com.payflowapplication.data.db.NotificacaoDao
import br.com.payflowapplication.data.db.PayFlowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SEED_FILE = "seed_assinaturas_sqlite.sql"
private const val TAG = "DatabaseModule"

/**
 * Lê o arquivo [SEED_FILE] dos assets e executa cada statement SQL
 * separando por ';'. É executado somente na criação do banco (onCreate).
 */
private fun seedCallback(context: Context) = object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        try {
            val sql = context.assets.open(SEED_FILE)
                .bufferedReader()
                .readText()

            // Divide os statements pelo delimitador ';', ignorando linhas vazias e comentários
            sql.split(";")
                .map { it.trim() }
                .filter { it.isNotEmpty() && !it.startsWith("--") }
                .forEach { statement ->
                    db.execSQL(statement)
                }

            Log.i(TAG, "Seed '$SEED_FILE' executado com sucesso.")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao executar seed '$SEED_FILE': ${e.message}", e)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PayFlowDatabase =
        Room.databaseBuilder(context, PayFlowDatabase::class.java, "payflow.db")
            .fallbackToDestructiveMigration()
            .addCallback(seedCallback(context))
            .build()

    @Provides
    fun provideAssinaturaDao(database: PayFlowDatabase): AssinaturaDao =
        database.assinaturaDao()

    @Provides
    fun provideHistoryDao(database: PayFlowDatabase): HistoryDao =
        database.historyDao()

    @Provides
    fun provideNotificacaoDao(database: PayFlowDatabase): NotificacaoDao =
        database.notificacaoDao()
}
