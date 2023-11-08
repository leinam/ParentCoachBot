package com.example.parentcoachbot.feature_chat.domain.util

import android.app.Application
import android.util.Log
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.SyncException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.sync.SyncSession
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RealmSyncRepository @Inject constructor(
    val application: Application, val app: App,
    val authManager: AuthManager,
    private val appPreferences: AppPreferences
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
            schema = setOf(
                Answer::class,
                Question::class, Topic::class,
                ChatSession::class, Subtopic::class,
                QuestionSession::class, ParentUser::class,
                ChildProfile::class, AnswerThread::class
            )
        )
            .maxNumberOfActiveVersions(10)
            .name("PCTest1")
            .errorHandler { session: SyncSession,
                            error: SyncException ->
                Log.println(Log.ERROR, "Realm", error.message + session)
            }
            .build()

        realm = Realm.open(config)
        Log.println(Log.INFO, "Realm", "Successfully opened realm: ${realm.configuration.name}")


    }

    suspend fun populateDatabase() {
        val isDbInit: Boolean = appPreferences.getIsDbInitialized()


        if (!isDbInit) {
            realm.write {
                currentUser?.let {

                    Log.println(Log.INFO, "DB", "Attempting to populate the database")
                    PopulateDb(
                        this,
                        application,
                        userId = it.id
                    )()

                    appPreferences.setIsDbInitialized(true)
                }
            }

        }
    }


}