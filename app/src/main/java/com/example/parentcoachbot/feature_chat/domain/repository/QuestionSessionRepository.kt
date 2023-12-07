package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import kotlinx.coroutines.flow.Flow

interface QuestionSessionRepository {

    suspend fun getAllQuestionSessions(): Flow<List<QuestionSession>>?
    suspend fun getAllSavedQuestionSessionsByProfile(childProfileId: String): Flow<List<QuestionSession>>?
    suspend fun newQuestionSession(questionSession: QuestionSession)
    suspend fun getQuestionSessionById(id: String): QuestionSession?
    suspend fun getQuestionSessionsByChatSession(chatSessionId: String): Flow<List<QuestionSession>>?
    suspend fun getLatestQuestionSessionByChatSession(chatSessionId: String): QuestionSession?
    suspend fun toggleSaveQuestionSession(id: String): Unit?
    suspend fun deleteQuestionSession(id: String)
    suspend fun deleteQuestionSessionsByChatSession(chatSessionId: String)
}