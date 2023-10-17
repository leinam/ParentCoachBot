package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface ChatSessionRepository {
    suspend fun newChatSession(chatSession: ChatSession)

    suspend fun deleteChatSession(id: ObjectId)

    suspend fun getChatSessionById(id: ObjectId): ChatSession?

    suspend fun togglePinChatSession(id: ObjectId)

    suspend fun getChatSessionsByChildProfile(childProfileId: ObjectId): Flow<List<ChatSession>>

    suspend fun getChatSessionsByChildProfileAsynch(childProfileId: ObjectId): Flow<ChatSession>

    suspend fun updateLastAnswerText(answer: Answer, chatSessionId: ObjectId)
    suspend fun updateChatTitle(subtopic: Subtopic, chatSessionId: ObjectId)


}