package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import io.realm.kotlin.types.RealmList

class GetAnswersByIdList(private val repository: AnswerRepository) {

    suspend operator fun invoke(questionAnswers: RealmList<String>): List<Answer>? {
        return repository.getQuestionAnswers(idList = questionAnswers)
    }

}