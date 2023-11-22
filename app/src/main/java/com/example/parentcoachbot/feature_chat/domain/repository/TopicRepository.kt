package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Topic
import kotlinx.coroutines.flow.Flow

interface TopicRepository {
    suspend fun getAllTopics(): Flow<List<Topic>>?
    suspend fun addTopic(topic: Topic)
}