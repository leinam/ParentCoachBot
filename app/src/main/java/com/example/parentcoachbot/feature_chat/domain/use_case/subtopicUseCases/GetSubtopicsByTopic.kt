package com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class GetSubtopicsByTopic (private val subtopicRepository: SubtopicRepository){
    suspend operator fun invoke(topicId: ObjectId): Flow<List<Subtopic>> {
        return subtopicRepository.getSubtopicsByTopic(topicId = topicId)
    }
}