package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface SubtopicRepository {
    suspend fun getAllSubtopics(): Flow<List<Subtopic>>

    suspend fun getSubtopicsByTopic(topicId: ObjectId): Flow<List<Subtopic>>

    suspend fun getSubtopicsById(id: ObjectId): Subtopic?

    suspend fun addSubtopic(subtopic: Subtopic)
}