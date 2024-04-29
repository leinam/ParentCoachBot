package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import kotlinx.coroutines.flow.Flow

interface SubtopicRepository {
    suspend fun getAllSubtopics(): Flow<List<Subtopic>>
    suspend fun getSubtopicsByTopic(topicId: String): Flow<List<Subtopic>>?
    suspend fun getSubtopicsById(id: String): Subtopic?
    suspend fun getSubtopicsByCode(code: String): Subtopic?

    suspend fun addSubtopic(subtopic: Subtopic)
}