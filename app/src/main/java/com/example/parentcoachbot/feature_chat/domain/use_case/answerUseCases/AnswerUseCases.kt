package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

data class AnswerUseCases(val getAnswer: GetAnswer,
                          val getAnswersByIdList: GetAnswersByIdList,
                          val getAnswerThreadLastAnswer: GetAnswerThreadLastAnswer
)
