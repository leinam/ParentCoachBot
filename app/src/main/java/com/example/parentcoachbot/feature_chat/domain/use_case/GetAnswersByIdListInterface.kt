package com.example.parentcoachbot.feature_chat.domain.use_case

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import io.realm.kotlin.types.RealmList

interface GetAnswersByIdListInterface {
    suspend operator fun invoke(questionAnswers: RealmList<String>): List<Answer>
}

class GetAnswersByIdList(private val repository: AnswerRepository) : GetAnswersByIdListInterface {
    override suspend operator fun invoke(questionAnswers: RealmList<String>): List<Answer> {
        return repository.getQuestionAnswers(idList = questionAnswers)
    }
}