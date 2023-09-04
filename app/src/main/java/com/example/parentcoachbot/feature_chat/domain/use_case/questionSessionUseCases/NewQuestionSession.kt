package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import org.mongodb.kbson.ObjectId

class NewQuestionSession (private val questionSessionRepository: QuestionSessionRepository){

    suspend operator fun invoke(chatSessionId: ObjectId,
                                question: Question){

            val questionSession = QuestionSession().apply {
                this.question = question._id
                this.chatSession = chatSessionId
            }

            questionSessionRepository.newQuestionSession(questionSession = questionSession)

        }
    }
