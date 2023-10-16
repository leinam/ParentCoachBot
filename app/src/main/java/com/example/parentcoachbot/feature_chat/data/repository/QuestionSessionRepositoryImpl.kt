package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class QuestionSessionRepositoryImpl(private val realm: Realm): QuestionSessionRepository {
    override suspend fun getAllQuestionSessions(): Flow<List<QuestionSession>> = withContext(Dispatchers.IO) {
        realm.query<QuestionSession>().find().asFlow().map {
            it.list
        }
    }

    override suspend fun getAllSavedQuestionSessions(): Flow<List<QuestionSession>>  = withContext(Dispatchers.IO) {
        realm.query<QuestionSession>("isSaved == $0", true).find().asFlow().map {
            it.list
        }
    }

    override suspend fun newQuestionSession(questionSession: QuestionSession) {
        realm.write {
            this.copyToRealm(questionSession)
        }
    }

    override suspend fun getQuestionSessionById(id: ObjectId): QuestionSession? = withContext(Dispatchers.IO){
        realm.query<QuestionSession>(query = "_id == $0", id).find().firstOrNull()
    }

    override suspend fun getQuestionSessionsByChatSession(chatSessionId: ObjectId): Flow<List<QuestionSession>> = withContext(
        Dispatchers.IO){
        realm.query<QuestionSession>(query = "chatSession == $0", chatSessionId).find().asFlow()
            .map { it.list }
    }

    override suspend fun getLatestQuestionSessionByChatSession(chatSessionId: ObjectId): QuestionSession = withContext(Dispatchers.IO){
        realm.query<QuestionSession>(query = "chatSession == $0", chatSessionId).find().last()
    }

    override suspend fun toggleSaveQuestionSession(id: ObjectId) = withContext(Dispatchers.IO) {
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

    override suspend fun deleteQuestionSession(id: ObjectId) {
        realm.write {
            val questionSession = realm.query<QuestionSession>(query = "id == $id").find().firstOrNull()
            questionSession?.let {
                delete(questionSession)
            }

        }
    }

}