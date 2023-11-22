package com.example.parentcoachbot.feature_chat.data.repository

import android.util.Log
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class QuestionSessionRepositoryImpl(private val realm: Realm) : QuestionSessionRepository {
    override suspend fun getAllQuestionSessions(): Flow<List<QuestionSession>>? =
        withContext(Dispatchers.IO) {
            try {
                realm.query<QuestionSession>().asFlow().map {
                    it.list.copyFromRealm()
                }
            } catch (e: Exception) {
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }


        }

    override suspend fun getAllSavedQuestionSessionsByProfile(childProfileId: String): Flow<List<QuestionSession>>? =
        withContext(Dispatchers.IO) {
            try {
                realm.query<QuestionSession>(
                    "isSaved == $0 AND childProfile == $1",
                    true,
                    childProfileId
                ).asFlow().map {
                    it.list.copyFromRealm().sortedWith(compareBy(
                        { it.chatSession }, { it.timeAsked })
                    )
                }
            } catch (e: Exception) {
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }
        }

    override suspend fun newQuestionSession(questionSession: QuestionSession): Unit =
        withContext(Dispatchers.IO) {
            realm.write {
                this.copyToRealm(questionSession)
            }

        }

    override suspend fun getQuestionSessionById(id: String): QuestionSession? =
        withContext(Dispatchers.IO) {

            try {
                realm.query<QuestionSession>(query = "_id == $0", id).find().firstOrNull()
            } catch (e: Exception) {
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }

        }

    override suspend fun getQuestionSessionsByChatSession(chatSessionId: String): Flow<List<QuestionSession>>? =
        withContext(
            Dispatchers.IO
        ) {
            try {
                realm.query<QuestionSession>(query = "chatSession == $0", chatSessionId)
                    .asFlow()
                    .map {
                        it.list.copyFromRealm()
                    }
            } catch (e: Exception) {
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }
        }

    override suspend fun getLatestQuestionSessionByChatSession(chatSessionId: String): QuestionSession? =
        withContext(Dispatchers.IO) {
            try {
                realm.query<QuestionSession>(query = "chatSession == $0", chatSessionId).find()
                    .last()
                    .copyFromRealm()
            } catch (e: Exception){
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }
        }

    override suspend fun toggleSaveQuestionSession(id: String) = withContext(Dispatchers.IO) {
        val questionSession =
            realm.query<QuestionSession>(query = "_id == $0", id).find()
                .firstOrNull()

        realm.write {
            questionSession?.let {
                findLatest(it)?.let { liveQuestionSession ->
                    val isSaved = !liveQuestionSession.isSaved
                    liveQuestionSession.isSaved = isSaved
                }
            }

        }

    }

    override suspend fun deleteQuestionSession(id: String) {
        realm.write {
            val questionSession =
                realm.query<QuestionSession>(query = "_id == $0", id).find()
                    .firstOrNull()
            questionSession?.let {
                findLatest(questionSession)?.also { delete(it) }
            }

        }
    }

}