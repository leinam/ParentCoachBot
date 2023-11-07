package com.example.parentcoachbot.di

import android.app.Application
import com.example.parentcoachbot.common.Constants
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.RealmSyncRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealmApp(): App {
        return App.create(Constants.APP_ID)
    }

    @Provides
    @Singleton
    fun provideRealmSyncRepository(
        app: App,
        application: Application,
        authManager: AuthManager
    ): RealmSyncRepository {


        return RealmSyncRepository(
            app = app,
            application = application,
            authManager = authManager
        )
    }

    @Provides
    @Singleton
    fun provideRealmSyncInstance(realmSyncRepository: RealmSyncRepository): Realm {
        return realmSyncRepository.realm
    }
}