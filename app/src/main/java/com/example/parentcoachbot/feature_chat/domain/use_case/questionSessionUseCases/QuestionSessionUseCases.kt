package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

data class QuestionSessionUseCases (val newQuestionSession: NewQuestionSession,
                                    val getChatQuestionSessions: GetQuestionSessionsByChatSession,
                                    val getLatestQuestionSessionByChat: GetLatestQuestionSessionByChat,
                                    val toggleSaveQuestionSession: ToggleSaveQuestionSession,
                                    val deleteQuestionSession: DeleteQuestionSession
)