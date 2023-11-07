package com.example.parentcoachbot.di

import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthManager(app: App): AuthManager {
        return AuthManager(app)
    }
}