package com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SavedQuestionStateWrapper(
    val savedQuestionsListState: StateFlow<List<QuestionSession>> = MutableStateFlow(emptyList()),
    val currentLanguageCode: StateFlow<String?> = MutableStateFlow("en"),
    val currentChildProfile: StateFlow<ChildProfile?> = MutableStateFlow(null),
    val savedQuestionSessionsWithQuestionAndAnswersState: StateFlow<List<Triple<QuestionSession,
            Question?, List<Answer>?>?>> = MutableStateFlow(
        emptyList()
    )
)