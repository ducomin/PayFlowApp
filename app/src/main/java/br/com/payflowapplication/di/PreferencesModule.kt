package br.com.payflowapplication.di

import android.content.Context
import br.com.payflowapplication.data.preferences.ThemePreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt que expõe [ThemePreferencesDataStore] como singleton.
 *
 * O DataStore precisa do [Context] da Application para escapar do escopo
 * de Activity/ViewModel e sobreviver ao fechamento do app.
 */
@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideThemePreferencesDataStore(
        @ApplicationContext context: Context,
    ): ThemePreferencesDataStore = ThemePreferencesDataStore(context)
}

