package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import org.mongodb.kbson.ObjectId

class GetQuestionWithAnswers(
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val answerThreadRepository: AnswerThreadRepository
) {

    suspend operator fun invoke(questionId: ObjectId): Pair<Question, List<Answer>>? {
        var questionAnswerPair: Pair<Question, List<Answer>>? = null

        questionRepository.getQuestionById(questionId)?.let { question ->

            question.answerThread?.let { answerThreadCode ->

                answerThreadRepository.getAnswerThreadByCode(answerThreadCode)?.code?.let {

                    answerRepository.getAnswersByAnswerThreadCode(it)?.let { answerList ->
                        questionAnswerPair = Pair(question, answerList)
                    }
                }

            }

        }


        // println("question pair $questionAnswerPair")

        return questionAnswerPair
    }
}