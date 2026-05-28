package br.com.payflowapplication.di

import android.content.Context
import br.com.payflowapplication.data.preferences.AuthPreferencesDataStore
import br.com.payflowapplication.data.preferences.ThemePreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideThemePreferencesDataStore(
        @ApplicationContext context: Context,
    ): ThemePreferencesDataStore = ThemePreferencesDataStore(context)

    @Provides
    @Singleton
    fun provideAuthPreferencesDataStore(
        @ApplicationContext context: Context,
    ): AuthPreferencesDataStore = AuthPreferencesDataStore(context)
}
