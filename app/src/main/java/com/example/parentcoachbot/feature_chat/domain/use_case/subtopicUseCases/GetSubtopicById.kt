package com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository
import org.mongodb.kbson.ObjectId

class GetSubtopicById(private val subtopicRepository: SubtopicRepository) {
    suspend operator fun invoke(subtopicId: ObjectId): Subtopic? {
        return subtopicRepository.getSubtopicsById(id = subtopicId)
    }
}