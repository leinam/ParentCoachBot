package com.example.parentcoachbot.feature_chat.domain.util

import android.util.Log
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.ConnectionException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.concurrent.thread

class AuthManager @Inject constructor(
    val app: App,
    val networkConnectionMangerImpl: NetworkConnectionMangerImpl
) {
    private val _authenticatedRealmUser: MutableStateFlow<User?> =
        MutableStateFlow(app.currentUser) // add to global state rather

    val authenticatedRealmUser: StateFlow<User?> = _authenticatedRealmUser

    init {
        networkConnectionMangerImpl.startListenNetworkState()

        thread {
            runBlocking {

                networkConnectionMangerImpl.isNetworkConnectedFlow.onEach { isNetworkConnected ->
                    println("connection: $isNetworkConnected")
                    if (isNetworkConnected) {
                        if (app.currentUser == null) {
                            println("Attempting Auth")
                            authenticateUser()
                        }
                    }
                }.collect()

            }
        }
    }

    private suspend fun authenticateUser(): AuthResult {
        val anonymousCredentials = Credentials.anonymous(reuseExisting = true)
        var authenticationResult: User? = null

        runCatching {
            authenticationResult = app.login(anonymousCredentials)
        }
            .onSuccess {
                if (authenticationResult?.loggedIn == true) {
                    _authenticatedRealmUser.value = app.currentUser

                    Log.println(
                        Log.INFO,
                        "Realm",
                        "Successful Authentication of User: " +
                                "${_authenticatedRealmUser.value?.id}"
                    )

                    // networkConnectionMangerImpl.stopListenNetworkState()
                    return AuthResult.Success(app.currentUser!!)
                }
            }
            .onFailure { exception: Throwable ->
                when (exception) {
                    is InvalidCredentialsException -> {
                        Log.println(
                            Log.ERROR,
                            "Realm",
                            "Failed to login due to invalid credentials: ${exception.message}"
                        )
                        return AuthResult.Failure(
                            AuthFailure.InvalidCredentials(
                                "Invalid username or password. Please try again."
                            )
                        )

                    }

                    is ConnectionException -> {
                        Log.println(
                            Log.ERROR,
                            "Realm",
                            "Failed to login due to a connection error: ${exception.message}"
                        )

                        return AuthResult.Failure(
                            AuthFailure.ConnectionError(
                                "Login failed due to a connection error. " +
                                        "Check your network connection and try again."
                            )
                        )
                    }

                    else -> {
                        Log.println(
                            Log.ERROR, "Realm",
                            "Failed to login: ${exception.message}"
                        )
                        return AuthResult.Failure(
                            AuthFailure.OtherError(
                                "Login failed please try again."
                            )
                        )

                    }
                }

            }
        return AuthResult.Failure(
            AuthFailure.InvalidCredentials(
                "Login failed please try again."
            )
        )

    }
}

sealed class AuthFailure {
    data class InvalidCredentials(val toastMessage: String) : AuthFailure()
    data class ConnectionError(val toastMessage: String) : AuthFailure()
    data class OtherError(val toastMessage: String) : AuthFailure()
}

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Failure(val authFailure: AuthFailure) : AuthResult()
}

