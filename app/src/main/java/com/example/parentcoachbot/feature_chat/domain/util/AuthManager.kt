package com.example.parentcoachbot.feature_chat.domain.util

import android.util.Log
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AuthManager @Inject constructor(val app: App) {
    val realmUser: MutableStateFlow<User?> = MutableStateFlow(app.currentUser)

    init {
        // TODO what if changes to null
    }

    suspend fun authenticateUser(): User {
        val anonymousCredentials = Credentials.anonymous(reuseExisting = true)
        val authenticationResult = app.login(anonymousCredentials)

        if (authenticationResult.loggedIn) {
            // Authentication succeeded, return the authenticated user
            realmUser.value = app.currentUser
            Log.println(Log.INFO, "Realm", "Authenticated User: ${realmUser.value?.id}")
            return app.currentUser!!
        } else {
            // Authentication failed, handle the error
            throw Exception("Authentication failed: $authenticationResult")
        }
    }
}