package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import org.mongodb.kbson.ObjectId

class GetQuestionsFromIdList(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(questionIdList: List<ObjectId>): List<Question> {
        return questionRepository.getQuestionsFromIdList(idList = questionIdList)
    }
}