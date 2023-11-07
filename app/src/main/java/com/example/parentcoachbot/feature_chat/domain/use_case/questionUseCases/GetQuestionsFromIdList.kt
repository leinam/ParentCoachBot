package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository

class GetQuestionsFromIdList(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(questionIdList: List<String>): List<Question> {
        return questionRepository.getQuestionsFromIdList(idList = questionIdList)
    }
}