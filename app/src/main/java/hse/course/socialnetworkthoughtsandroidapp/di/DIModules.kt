package hse.course.socialnetworkthoughtsandroidapp.di

import android.content.Context
import android.content.SharedPreferences

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import hse.course.socialnetworkthoughtsandroidapp.api.AuthenticationService
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DIModules {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPreferencesKeys.STORAGE.name,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideAuthenticationService(): AuthenticationService {
        return AuthenticationService.create()
    }
}