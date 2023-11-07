package com.example.parentcoachbot.feature_chat.domain.util

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.SyncException
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.sync.SyncSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RealmSyncRepository @Inject constructor(
    val application: Application, val app: App, val authManager: AuthManager
) {

    lateinit var realm: Realm
    private var currentUser: User? = null


    init {
        runBlocking {
            currentUser = app.currentUser
            if (currentUser == null) {
                currentUser = authManager.authenticateUser()
            }
        }

        currentUser?.let {
            if (it.loggedIn) {
                configureRealmSync(it)
            }
        }

    }

    private fun configureRealmSync(currentUser: User) {
        val config = SyncConfiguration.Builder(
            user = currentUser,
            partitionValue = currentUser.id,
            schema = setOf()
        )
            .maxNumberOfActiveVersions(10)
            .name("PCTest")
            .initialData() {
                PopulateDb(this, application)
            }
            .errorHandler { session: SyncSession,
                            error: SyncException ->
                println(error.message + session)
            }
            .build()

        realm = Realm.open(config)

        CoroutineScope(Dispatchers.Main).launch {
            realm.subscriptions.waitForSynchronization()
        }
    }


}