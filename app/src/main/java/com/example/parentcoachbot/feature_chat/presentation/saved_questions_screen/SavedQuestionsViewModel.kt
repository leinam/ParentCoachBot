package com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.util.RealmInstantConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SavedQuestionsViewModel @Inject constructor(
    private val eventLogger: EventLogger,
    private val questionUseCases: QuestionUseCases,
    private val questionSessionUseCases: QuestionSessionUseCases,
    private val globalState: GlobalState,
) : ViewModel() {

    private val _savedQuestions: MutableStateFlow<List<QuestionSession>> = MutableStateFlow(
        emptyList()
    )
    private var getSavedQuestionSessionsJob: Job? = null
    private var getSavedQuestionSessionWithQuestionAndAnswersJob: Job? = null
    private var getSavedQuestionSessionWithQuestionAndAnswersGroupedByDateJob: Job? = null

    private val _currentChildProfile = globalState.currentChildProfileState
    private val _questionSessionsWithQuestionAndAnswersListState:
            MutableStateFlow<List<Triple<QuestionSession, Question?, List<Answer>?>?>> =
        MutableStateFlow(emptyList())
    private val _questionSessionsWithQuestionAndAnswersListGroupedByDateState:
            MutableStateFlow<Map<LocalDate, List<Triple<QuestionSession, Question?, List<Answer>?>?>>> =
        MutableStateFlow(emptyMap())

    val savedChatViewModelState: State<SavedQuestionStateWrapper> = mutableStateOf(
        SavedQuestionStateWrapper(
            savedQuestionsListState = _savedQuestions,
            currentLanguageCode = globalState.currentLanguageCode,
            currentChildProfile = globalState.currentChildProfileState,
            savedQuestionSessionsWithQuestionAndAnswersState = _questionSessionsWithQuestionAndAnswersListState,
            savedQuestionSessionsWithQuestionAndAnswersListGroupedByDateState = _questionSessionsWithQuestionAndAnswersListGroupedByDateState

        )
    )


    init {
        getSavedQuestionSessions()
        getSavedQuestionSessionWithQuestionAndAnswers()
        getSavedQuestionSessionWithQuestionAndAnswersGroupedByDate()
    }

    private fun getSavedQuestionSessions() {
        getSavedQuestionSessionsJob?.cancel()

        getSavedQuestionSessionsJob = viewModelScope.launch {
            _currentChildProfile.onEach { currentChildProfile ->
                // println("current ${currentChildProfile?._id}")
                currentChildProfile?.let {
                    questionSessionUseCases.getSavedQuestionSessionsByProfile(it._id)
                        ?.onEach { questionSessionList ->
                            _savedQuestions.value = questionSessionList
                            println(_savedQuestions.value + "saved questions")
                        }?.collect()
                }
            }.collect()
        }
    }

    private fun getSavedQuestionSessionWithQuestionAndAnswers() {
        getSavedQuestionSessionWithQuestionAndAnswersJob?.cancel()

        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var questionSessionWithQuestionAndAnswersList: List<Triple<QuestionSession, Question?, List<Answer>?>?>

        getSavedQuestionSessionWithQuestionAndAnswersJob = viewModelScope.launch {

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
                println(questionSessionWithQuestionAndAnswersList + "bundle")
                _questionSessionsWithQuestionAndAnswersListState.value =
                    questionSessionWithQuestionAndAnswersList

            }.collect()
        }


    }

    private fun getSavedQuestionSessionWithQuestionAndAnswersGroupedByDate() {
        getSavedQuestionSessionWithQuestionAndAnswersGroupedByDateJob?.cancel()

        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var questionSessionWithQuestionAndAnswersListGroupedByDate: Map<LocalDate, List<Triple<QuestionSession, Question?, List<Answer>?>?>>

        getSavedQuestionSessionWithQuestionAndAnswersGroupedByDateJob = viewModelScope.launch {

            _savedQuestions.onEach { questionSessionList ->
                val questionSessionListGroupedByDate =
                    questionSessionList.groupBy { RealmInstantConverter.toLocalDate(it.timeSaved ?: it.timeAsked) }
                //TODO check grouping logic

                questionSessionWithQuestionAndAnswersListGroupedByDate =
                    questionSessionListGroupedByDate.mapValues {
                        it.value.map { questionSession ->

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

                    }


                _questionSessionsWithQuestionAndAnswersListGroupedByDateState.value =
                    questionSessionWithQuestionAndAnswersListGroupedByDate

            }.collect()
        }


    }

    fun onEvent(event: SavedQuestionsScreenEvent) {
        when (event) {
            is SavedQuestionsScreenEvent.SaveQuestionSession -> {
                viewModelScope.launch {
                    questionSessionUseCases.toggleSaveQuestionSession(event.questionSessionId)
                }
            }

            is SavedQuestionsScreenEvent.DeleteQuestionSession -> {
                eventLogger.logComposableLoad("")
                viewModelScope.launch {
                    questionSessionUseCases.deleteQuestionSession(event.questionSession._id)
                }
            }

            SavedQuestionsScreenEvent.UpdateChildProfile -> {
                getSavedQuestionSessions()
            }
        }
    }
}

