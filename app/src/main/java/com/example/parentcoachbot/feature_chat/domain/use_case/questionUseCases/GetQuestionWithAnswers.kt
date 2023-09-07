package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswersByIdList
import org.mongodb.kbson.ObjectId

class GetQuestionWithAnswers (private val questionRepository: QuestionRepository,
                               private val answerRepository: AnswerRepository) {


    suspend operator fun invoke(questionId: ObjectId): Pair<Question, List<Answer>>? {
        var questionAnswerPair: Pair<Question, List<Answer>>? = null

        questionRepository.getQuestionById(questionId)?.let { question ->
            question.questionAnswers.let {
                val answers: List<Answer> = GetAnswersByIdList(
                    repository = answerRepository).invoke(it)
                questionAnswerPair = Pair(question, answers)
            }
        }
        return questionAnswerPair
    }
}