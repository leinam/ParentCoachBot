package com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository
import kotlinx.coroutines.flow.Flow

class GetSubtopicsByTopic (private val subtopicRepository: SubtopicRepository){
    suspend operator fun invoke(topicId: String): Flow<List<Subtopic>> {
        return subtopicRepository.getSubtopicsByTopic(topicId = topicId)
    }
}