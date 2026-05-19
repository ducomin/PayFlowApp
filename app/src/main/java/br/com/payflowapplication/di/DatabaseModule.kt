package br.com.payflowapplication.di

import android.content.Context
import androidx.room.Room
import br.com.payflowapplication.data.db.AssinaturaDao
import br.com.payflowapplication.data.db.PayFlowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PayFlowDatabase =
        Room.databaseBuilder(context, PayFlowDatabase::class.java, "payflow.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAssinaturaDao(database: PayFlowDatabase): AssinaturaDao =
        database.assinaturaDao()
}

