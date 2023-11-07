package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class QuestionSessionRepositoryImpl(private val realm: Realm): QuestionSessionRepository {
    override suspend fun getAllQuestionSessions(): Flow<List<QuestionSession>> = withContext(Dispatchers.IO) {
        realm.query<QuestionSession>().asFlow().map {
            it.list
        }
    }

    override suspend fun getAllSavedQuestionSessionsByProfile(): Flow<List<QuestionSession>>  = withContext(Dispatchers.IO) {
        realm.query<QuestionSession>("isSaved == $0", true).asFlow().map {
            it.list
        }
    }

    override suspend fun newQuestionSession(questionSession: QuestionSession) {
        realm.write {
            this.copyToRealm(questionSession)
        }
    }

    override suspend fun getQuestionSessionById(id: String): QuestionSession? = withContext(Dispatchers.IO){
        realm.query<QuestionSession>(query = "_id == $0", id).find().firstOrNull()
    }

    override suspend fun getQuestionSessionsByChatSession(chatSessionId: String): Flow<List<QuestionSession>> = withContext(
        Dispatchers.IO){
        realm.query<QuestionSession>(query = "chatSession == $0", chatSessionId).asFlow()
            .map { it.list }
    }

    override suspend fun getLatestQuestionSessionByChatSession(chatSessionId: String): QuestionSession = withContext(Dispatchers.IO){
        realm.query<QuestionSession>(query = "chatSession == $0", chatSessionId).find().last()
    }

    override suspend fun toggleSaveQuestionSession(id: String) = withContext(Dispatchers.IO) {
        realm.write {
            val questionSession =
                realm.query<QuestionSession>(query = "_id == $0", id).find()
                    .firstOrNull()
            questionSession?.let {
                val isSaved = !questionSession.isSaved
                questionSession.isSaved = isSaved
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