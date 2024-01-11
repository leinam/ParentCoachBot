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


    init { // TODO move to viewmodel
        runBlocking {
            currentUser = app.currentUser
        }

        currentUser?.let {
            if (it.loggedIn) {
                configureRealmSync(it)
            }
        }

    }

    private fun configureRealmSync(currentUser: User) {
        Log.println(Log.INFO, "Realm", "Configuring realm instance")
        if (::realm.isInitialized) {
            println("closing existing realm")
            realm.close()
        }


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
            .compactOnLaunch(callback = { totalBytes, usedBytes ->
                val thresholdSize = (50 * 1024 * 1024).toLong()
                print("totalBYTES: $totalBytes usedBytes: $usedBytes")
                totalBytes > thresholdSize && usedBytes.toDouble() / totalBytes.toDouble() >= 0.5
            }
            )
            .name("PCTest1")
            .errorHandler { session: SyncSession,
                            error: SyncException ->
                Log.println(Log.ERROR, "Realm", error.message + session)
            }
            .build()

        kotlin.runCatching {
            realm = Realm.open(config)
        }.onSuccess {
            Log.println(Log.INFO, "Realm", "Successfully opened realm: ${realm.configuration.name}")

        }


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