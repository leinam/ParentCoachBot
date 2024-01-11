package com.example.parentcoachbot.di

import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.NetworkConnectionMangerImpl
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
    fun provideAuthManager(app: App, networkConnectionMangerImpl: NetworkConnectionMangerImpl): AuthManager {
        return AuthManager(app, networkConnectionMangerImpl = networkConnectionMangerImpl)
    }
}