package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswersByIdList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuestionsWithAnswers (private val questionRepository: QuestionRepository,
                               private val answerRepository: AnswerRepository){


    suspend operator fun invoke() : Flow<MutableList<Pair<Question, List<Answer>>?>> {
        val questionsWithAnswersList: MutableList<Pair<Question, List<Answer>>?> = mutableListOf()

        return questionRepository.getAllQuestions().map {
                questionsList ->
            questionsList.forEach {
                    question: Question ->
                question.questionAnswers.let {

                    var questionAnswerPair: Pair<Question, List<Answer>>?
                    val answers: List<Answer> = GetAnswersByIdList(
                        repository = answerRepository).invoke(it)


                    questionAnswerPair = Pair(
                        question, answers)
                    questionsWithAnswersList.add(questionAnswerPair)


                }
            }
            questionsWithAnswersList
        }


    }
}