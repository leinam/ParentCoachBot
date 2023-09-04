package com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow

class GetAllTopics(private val repository: TopicRepository) {
    suspend operator fun invoke(): Flow<List<Topic>> {
        return repository.getAllTopics()
    }
}