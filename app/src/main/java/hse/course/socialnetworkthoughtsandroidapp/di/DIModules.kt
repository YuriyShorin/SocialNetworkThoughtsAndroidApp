package hse.course.socialnetworkthoughtsandroidapp.di

import android.content.Context
import android.content.SharedPreferences

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import hse.course.socialnetworkthoughtsandroidapp.api.AuthenticationService
import hse.course.socialnetworkthoughtsandroidapp.api.FeedService
import hse.course.socialnetworkthoughtsandroidapp.api.PostService
import hse.course.socialnetworkthoughtsandroidapp.api.ProfileService
import hse.course.socialnetworkthoughtsandroidapp.api.SearchService
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

    @Singleton
    @Provides
    fun provideProfileService(): ProfileService {
        return ProfileService.create()
    }

    @Singleton
    @Provides
    fun providePostService(): PostService {
        return PostService.create()
    }

    @Singleton
    @Provides
    fun provideSearchService(): SearchService {
        return SearchService.create()
    }

    @Singleton
    @Provides
    fun provideFeedService(): FeedService {
        return FeedService.create()
    }
}