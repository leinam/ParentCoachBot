package com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository

class GetSubtopicByCode(private val subtopicRepository: SubtopicRepository) {
    suspend operator fun invoke(code: String): Subtopic? {
        return subtopicRepository.getSubtopicsByCode(code = code)
    }
}