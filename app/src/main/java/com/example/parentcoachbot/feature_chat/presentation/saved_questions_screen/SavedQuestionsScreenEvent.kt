package com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession

sealed class SavedQuestionsScreenEvent {
    data class DeleteQuestionSession(val questionSession: QuestionSession) :
        SavedQuestionsScreenEvent()

    data class SaveQuestionSession(val questionSessionId: String) :
        SavedQuestionsScreenEvent()
}
