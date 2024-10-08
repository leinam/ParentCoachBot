package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository

class NewQuestionSession (private val questionSessionRepository: QuestionSessionRepository){

    suspend operator fun invoke(
        chatSessionId: String,
        question: Question,
        childProfile: String?,
        userId: String?
    ){

            val questionSession = QuestionSession().apply {
                this.question = question._id
                this.chatSession = chatSessionId
                this.childProfile = childProfile
                this._partition = userId
            }

            questionSessionRepository.newQuestionSession(questionSession = questionSession)

        }
    }
