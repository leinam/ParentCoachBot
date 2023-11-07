package com.example.parentcoachbot.feature_chat.domain.util

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import javax.inject.Inject

class AuthManager @Inject constructor(val app: App) {

    suspend fun authenticateUser(): User {
        val anonymousCredentials = Credentials.anonymous(reuseExisting = true)
        val authenticationResult = app.login(anonymousCredentials)

        if (authenticationResult.loggedIn) {
            // Authentication succeeded, return the authenticated user
            return app.currentUser!!
        } else {
            // Authentication failed, handle the error
            throw Exception("Authentication failed: $authenticationResult")
        }
    }
}