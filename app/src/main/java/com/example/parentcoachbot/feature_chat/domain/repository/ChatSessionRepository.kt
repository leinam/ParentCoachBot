package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import kotlinx.coroutines.flow.Flow

interface ChatSessionRepository {
    suspend fun newChatSession(chatSession: ChatSession)

    suspend fun deleteChatSession(id: String)

    suspend fun getChatSessionById(id: String): ChatSession?

    suspend fun togglePinChatSession(id: String)

    suspend fun getChatSessionsByChildProfile(childProfileId: String): Flow<List<ChatSession>>

    suspend fun getChatSessionsByChildProfileAsynch(childProfileId: String): Flow<ChatSession>

    suspend fun updateLastAnswerText(answer: Answer, chatSessionId: String)

    suspend fun updateTimeLastUpdated(chatSessionId: String)

    suspend fun updateChatTitle(subtopic: Subtopic, chatSessionId: String)


}