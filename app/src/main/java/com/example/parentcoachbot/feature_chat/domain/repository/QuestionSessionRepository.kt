package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface QuestionSessionRepository {

    suspend fun getAllQuestionSessions(): Flow<List<QuestionSession>>
    suspend fun getAllSavedQuestionSessionsByProfile(): Flow<List<QuestionSession>>
    suspend fun newQuestionSession(questionSession: QuestionSession)

    suspend fun getQuestionSessionById(id: ObjectId): QuestionSession?

    suspend fun getQuestionSessionsByChatSession(chatSessionId: ObjectId): Flow<List<QuestionSession>>

    suspend fun getLatestQuestionSessionByChatSession(chatSessionId: ObjectId): QuestionSession?

    suspend fun toggleSaveQuestionSession(id: ObjectId): Unit?

    suspend fun deleteQuestionSession(id: ObjectId)

}