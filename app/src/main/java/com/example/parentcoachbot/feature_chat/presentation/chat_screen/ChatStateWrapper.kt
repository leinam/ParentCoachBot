package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import android.app.Application
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mongodb.kbson.ObjectId

data class ChatStateWrapper(
    val subtopicQuestionsListState: StateFlow<List<Question>> = MutableStateFlow(emptyList()),
    var questionSessionListState: StateFlow<List<QuestionSession>> = MutableStateFlow(emptyList()),
    val questionsWithAnswersState: StateFlow<MutableList<Pair<Question,
            List<Answer>>?>> = MutableStateFlow(mutableListOf()),
    val topicsListState: StateFlow<List<Topic>> = MutableStateFlow(emptyList()),
    val subtopicsListState: StateFlow<List<Subtopic>> = MutableStateFlow(emptyList()),
    val currentChildProfile: StateFlow<ChildProfile?> = MutableStateFlow(null),
    val application: StateFlow<Application?> = MutableStateFlow(null),
    val chatSessionId: ObjectId? = null,
    val questionSessionsWithQuestionAndAnswersState: StateFlow<List<Triple<QuestionSession,
            Question?, List<Answer>?>?>> = MutableStateFlow(
        emptyList()
    ),
    val searchQueryText: StateFlow<String> = MutableStateFlow(""),
    val searchResultsQuestionsListState: StateFlow<List<Question>> = MutableStateFlow(emptyList()),
    val currentTopicState: StateFlow<Topic?> = MutableStateFlow(null),
    val currentSubtopic: StateFlow<Subtopic?> = MutableStateFlow(null),
    val currentLanguageCode : StateFlow<String?> = MutableStateFlow(null)
)
