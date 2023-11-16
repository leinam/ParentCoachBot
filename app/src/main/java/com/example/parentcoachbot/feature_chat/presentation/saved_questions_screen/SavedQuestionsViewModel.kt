package com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.AnswerUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.SubtopicUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedQuestionsViewModel @Inject constructor(
    private val application: Application,
    val firebaseAnalytics: FirebaseAnalytics,
    private val eventLogger: EventLogger,
    private val questionUseCases: QuestionUseCases,
    private val questionSessionUseCases: QuestionSessionUseCases,
    private val topicUseCases: TopicUseCases,
    private val subtopicUseCases: SubtopicUseCases,
    private val childProfileUseCases: ChildProfileUseCases,
    private val parentUserUseCases: ParentUserUseCases,
    private val answerUseCases: AnswerUseCases,
    private val chatSessionUseCases: ChatSessionUseCases,
    private val globalState: GlobalState,
) : ViewModel() {

    private val _savedQuestions: MutableStateFlow<List<QuestionSession>> = MutableStateFlow(
        emptyList()
    )
    private val _questionSessionsWithQuestionAndAnswersListState:
            MutableStateFlow<List<Triple<QuestionSession, Question?, List<Answer>?>?>> =
        MutableStateFlow(emptyList())

    val savedChatViewModelState: State<SavedQuestionStateWrapper> = mutableStateOf(
        SavedQuestionStateWrapper(
            savedQuestionsListState = _savedQuestions,
            currentLanguageCode = globalState.currentLanguageCode,
            currentChildProfile = globalState.currentChildProfileState
        )
    )


    init {
        getSavedQuestionSessions()
        getQuestionSessionWithQuestionAndAnswers()
    }

    private fun getSavedQuestionSessions() {
        viewModelScope.launch {
            globalState.currentChildProfileState.onEach {
                it?.let { currentChildProfile ->
                    questionSessionUseCases.getSavedQuestionSessionsByProfile(currentChildProfile._id)
                        .onEach { questionSessionList ->
                            _savedQuestions.value = questionSessionList
                        }.collect()
                }
            }.collect()
        }
    }

    private fun getQuestionSessionWithQuestionAndAnswers() {
        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var questionSessionWithQuestionAndAnswersList: List<Triple<QuestionSession, Question?, List<Answer>?>?>

        viewModelScope.launch {

            _savedQuestions.onEach { questionSessionList ->
                questionSessionWithQuestionAndAnswersList =
                    questionSessionList.map { questionSession ->

                        questionSession.question?.let { questionId ->
                            questionWithAnswer =
                                questionUseCases.getQuestionWithAnswers(questionId)
                        }

                        Triple(
                            questionSession,
                            questionWithAnswer?.first,
                            questionWithAnswer?.second
                        )
                    }
                // println(questionSessionWithQuestionAndAnswersList)
                _questionSessionsWithQuestionAndAnswersListState.value =
                    questionSessionWithQuestionAndAnswersList
            }.collect()
        }


    }
}

