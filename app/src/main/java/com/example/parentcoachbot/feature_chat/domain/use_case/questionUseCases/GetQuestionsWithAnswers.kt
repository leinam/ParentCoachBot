package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuestionsWithAnswers(
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val answerThreadRepository: AnswerThreadRepository
) {


    suspend operator fun invoke(): Flow<MutableList<Pair<Question, List<Answer>>?>>? {
        val questionsWithAnswersList: MutableList<Pair<Question, List<Answer>>?> = mutableListOf()

        return questionRepository.getAllQuestions()?.map { questionsList ->
            questionsList.forEach { question: Question ->
                question.answerThread?.let {
                    answerThreadRepository.getAnswerThreadByCode(it).let { answerThread ->
                        answerThread?.code?.let { code ->
                            answerRepository.getAnswersByAnswerThreadCode(code).let { answersFlow ->
                                var questionAnswerPair: Pair<Question, List<Answer>>?

                                answersFlow?.let { answers ->
                                    questionAnswerPair = Pair(
                                        question, answers
                                    )
                                    questionsWithAnswersList.add(questionAnswerPair)
                                }
                            }
                        }

                    }

                }
            }
            questionsWithAnswersList
        }


    }
}