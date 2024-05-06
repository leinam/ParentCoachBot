package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.common.LoggingEvent
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.SearchQuery
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases.AnswerThreadUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.AnswerUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.searchQueryUseCases.SearchQueryUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.SubtopicUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AppPreferences
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.domain.util.QuestionSearcher
import com.example.parentcoachbot.feature_chat.domain.util.RealmInstantConverter
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

// TODO SEPARATE CHAT STATES
@OptIn(FlowPreview::class)
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val application: Application,
    private val appPreferences: AppPreferences,
    val firebaseAnalytics: FirebaseAnalytics,
    private val eventLogger: EventLogger,
    private val questionUseCases: QuestionUseCases,
    private val questionSessionUseCases: QuestionSessionUseCases,
    private val topicUseCases: TopicUseCases,
    private val subtopicUseCases: SubtopicUseCases,
    private val childProfileUseCases: ChildProfileUseCases,
    private val parentUserUseCases: ParentUserUseCases,
    private val answerUseCases: AnswerUseCases,
    private val answerThreadUseCases: AnswerThreadUseCases,
    private val chatSessionUseCases: ChatSessionUseCases,
    private val searchQueryUseCases: SearchQueryUseCases,
    private val globalState: GlobalState,
    private val authManager: AuthManager,
    private val questionSearcher: QuestionSearcher
) : ViewModel() {
    private val parentUserState = globalState.parentUserState
    private val _topicsListState = MutableStateFlow<List<Topic>>(emptyList())
    private val _currentChatState = MutableStateFlow<ChatSession?>(null)
    private val _childProfilesListState = MutableStateFlow<List<ChildProfile>>(emptyList())
    private val _subtopicsListState = MutableStateFlow<List<Subtopic>>(emptyList())
    private val _questionSessionListState = MutableStateFlow<List<QuestionSession>>(emptyList())
    private val _questionSessionListGroupedByDateState =
        MutableStateFlow<Map<LocalDate, List<QuestionSession>>>(
            emptyMap()
        )
    private val _subtopicQuestionsListState = MutableStateFlow<List<Question>>(emptyList())
    private val _questionsWithAnswersListState =
        MutableStateFlow<MutableList<Pair<Question, List<Answer>>?>>(mutableListOf())
    private val _questionSessionsWithQuestionAndAnswersListState: MutableStateFlow<List<Triple<QuestionSession, Question?, List<Answer>?>?>> =
        MutableStateFlow(
            emptyList()
        )
    private val _questionSessionsWithQuestionAndAnswersListGroupedByDateState: MutableStateFlow<Map<LocalDate, List<Triple<QuestionSession, Pair<Question?, List<Question?>?>, List<Answer>?>?>>> =
        MutableStateFlow(emptyMap())
    private val _currentTopic: MutableStateFlow<Topic?> = MutableStateFlow(null)
    private val _currentSubtopic: MutableStateFlow<Subtopic?> = MutableStateFlow(null)
    private val _typedQueryText = MutableStateFlow("")
    private val _newChatSession = globalState.newChatState
    private val _currentChildProfile = globalState.currentChildProfileState
    private val _currentLanguageCode = globalState.currentLanguageCode
    private val _searchResultsQuestionsListState = MutableStateFlow<List<Question>>(emptyList())
    private val _allQuestionsListState = MutableStateFlow<List<Question>>(emptyList())

    private val _chatViewModelState = mutableStateOf(
        ChatStateWrapper(
            subtopicQuestionsListState = _subtopicQuestionsListState,
            topicsListState = _topicsListState,
            questionsWithAnswersState = _questionsWithAnswersListState,
            questionSessionListState = _questionSessionListState,
            questionSessionListGroupedByDateState = _questionSessionListGroupedByDateState,
            subtopicsListState = _subtopicsListState,
            currentChildProfile = _currentChildProfile,
            questionSessionsWithQuestionAndAnswersState = _questionSessionsWithQuestionAndAnswersListState,
            questionSessionsWithQuestionAndAnswersGroupedByDateState = _questionSessionsWithQuestionAndAnswersListGroupedByDateState,
            searchResultsQuestionsListState = _searchResultsQuestionsListState,
            currentTopicState = _currentTopic,
            application = MutableStateFlow(application),
            currentLanguageCode = _currentLanguageCode
        )
    )

    val chatViewModelState: State<ChatStateWrapper> = _chatViewModelState

    private var lastDeletedQuestion: QuestionSession? = null
    private var getSubtopicQuestionsJob: Job? = null // track job coroutine observing db
    private var getTopicsJob: Job? = null // track job coroutine observing db
    private var getSubtopicsJob: Job? = null // track job coroutine observing db
    private var getQuestionsWithAnswersJob: Job? = null // track job coroutine observing db
    private var getChatQuestionSessionsJob: Job? = null
    private var getChildProfilesJob: Job? = null
    private var getAllQuestionsJob: Job? = null
    private var listenForNewChatJob: Job? = null
    private var listenForSearchQueryJob: Job? = null
    private var getCurrentLanguageJob: Job? = null
    private var getQuestionSessionWithQuestionAndAnswersJob: Job? = null
    private var getQuestionSessionWithQuestionAndAnswersGroupedByDateJob: Job? = null
    private var getChatQuestionSessionsGroupedByDateJob: Job? = null


    init {
        getChatQuestionSessions()
        getChatQuestionSessionsGroupedByDate()
        getTopics()
        getAllQuestions()
        getQuestionsWithAnswers()
        getChildProfiles()
        listenForNewChat()
        listenForSearchQuery()
        getCurrentLanguage()
    }

    private fun listenForNewChat() {
        listenForNewChatJob?.cancel()

        listenForNewChatJob = viewModelScope.launch {
            _newChatSession.onEach {
                // println("new ${_newChatSession.value?._id}")
                _currentChatState.value = it
                getQuestionSessionsWithQuestionAndAnswers()
                getQuestionSessionsWithQuestionAndAnswersGroupedByDate()
            }.collect()
        }
    }


    private fun listenForSearchQuery() {
        listenForSearchQueryJob?.cancel()

        listenForSearchQueryJob = viewModelScope.launch {
            _typedQueryText.debounce(1000).onEach {

                val trimmedQuery = it.trim()
                searchQuestions(trimmedQuery, _currentLanguageCode.value)


                if (!trimmedQuery.isNullOrBlank() && trimmedQuery.length > 2) {
                    parentUserState.value?.let { parentUser ->
                        eventLogger.logSearchEvent(
                            loggingEvent = LoggingEvent.NewSearchQuery,
                            searchQueryText = it,
                            chatSession = _currentChatState.value,
                            parentUser = parentUser,
                            profile = _currentChildProfile.value,
                            currentLanguage = _currentLanguageCode.value
                        )


                        searchQueryUseCases.newSearchQuery(
                            searchQuery = SearchQuery().apply {
                                this.owner_id = authManager.authenticatedRealmUser.value?.id
                                chatSession = _currentChatState.value?._id
                                queryText = it
                                parentUsername = parentUser.username
                                profile = _currentChildProfile.value?.name
                                currentLanguage = _currentLanguageCode.value
                            })

                    }
                }


            }.collect()
        }
    }

    private fun getCurrentLanguage() {
        getCurrentLanguageJob?.cancel()

        getCurrentLanguageJob = viewModelScope.launch {
            _currentLanguageCode.onEach {
                populateQuestionIndex(currentLanguage = it)
            }.collect()
        }
    }


    private fun populateQuestionIndex(currentLanguage: String = Language.English.isoCode) {
        println("Populating Question Index")
        viewModelScope.launch {
            _allQuestionsListState.onEach {
                questionSearcher.populateIndex(it, currentLanguage)
            }.collect()
        }
    }


    private fun searchQuestions(
        queryText: String,
        currentLanguage: String = Language.English.isoCode
    ) {

        val searchResult = questionSearcher.search(queryText = queryText.trim(), currentLanguage)

        viewModelScope.launch {
            if (searchResult.isNotEmpty()) {
                // println("Search result for query: $queryText is $searchResult")
                questionUseCases.getQuestionsFromIdList(searchResult)?.let {
                    _searchResultsQuestionsListState.value = it
                }

                // fix this
            } else {
                _searchResultsQuestionsListState.value = emptyList()
            }
        }
        // keep state for last search query and then when question selected
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SaveQuestionSession -> {
                viewModelScope.launch {
                    val isSaved =
                        questionSessionUseCases.toggleSaveQuestionSession(event.questionSession._id)

                    event.questionSession.question?.let { questionId ->

                        val question: Question? =
                            questionUseCases.getQuestionById(questionId = questionId)

                        if (isSaved == true) {
                            eventLogger.logQuestionEvent(
                                loggingEvent = LoggingEvent.SaveQuestion,
                                question = question,
                                questionSession = event.questionSession,
                                parentUser = parentUserState.value!!,
                                profile = _currentChildProfile.value!!
                            )
                        } else {
                            eventLogger.logQuestionEvent(
                                loggingEvent = LoggingEvent.UnsaveQuestion,
                                question = question,
                                questionSession = event.questionSession,
                                parentUser = parentUserState.value!!,
                                profile = _currentChildProfile.value!!
                            )
                        }


                    }
                }


            }

            is ChatEvent.DeleteQuestionSession -> {
                viewModelScope.launch {
                    lastDeletedQuestion = event.questionSession
                    questionSessionUseCases.deleteQuestionSession(event.questionSession._id)

                    event.questionSession.question?.let { questionId ->
                        val question: Question? =
                            questionUseCases.getQuestionById(questionId = questionId)

                        eventLogger.logQuestionEvent(
                            loggingEvent = LoggingEvent.DeleteQuestion,
                            question = question,
                            questionSession = event.questionSession,
                            parentUser = parentUserState.value!!,
                            profile = _currentChildProfile.value!!
                        )
                    }


                }
            }

            is ChatEvent.GetParent -> {
                viewModelScope.launch {
                    globalState.getParentUser()
                }
            }

            is ChatEvent.AddQuestionSession -> {
                viewModelScope.launch {
                    _currentChatState.value?.let { chatSession ->
                        questionSessionUseCases.newQuestionSession(
                            chatSessionId = chatSession._id,
                            question =
                            event.question,
                            userId = authManager.authenticatedRealmUser.value?.id,
                            childProfile = _currentChildProfile.value?._id,
                            parentUsername = parentUserState.value?.username
                        ).also {
                            event.question.subtopic?.let {
                                subtopicUseCases.getSubtopicByCode(it)?.let { subtopic ->
                                    chatSessionUseCases.updateChatTitle(
                                        subtopic = subtopic,
                                        chatSessionId = chatSession._id
                                    )
                                }
                            }

                            event.question.answerThread?.let { answerThreadCode ->

                                answerUseCases.getAnswerThreadLastAnswer(answerThreadCode = answerThreadCode)
                                    ?.let { answer ->
                                        chatSessionUseCases.updateChatLastAnswerText(
                                            answer = answer, chatSessionId = chatSession._id
                                        )
                                    }
                            }

                            chatSessionUseCases.updateChatTimeLastUpdated(chatSession._id)

                            if (event.previousQuestion != null) {
                                eventLogger.logQuestionEvent(
                                    loggingEvent = LoggingEvent.AddRelatedQuestion,
                                    question = event.question,
                                    previousQuestion = event.previousQuestion,
                                    questionSession = null,
                                    parentUser = parentUserState.value!!,
                                    profile = _currentChildProfile.value!!
                                )
                            } else {
                                eventLogger.logQuestionEvent(
                                    loggingEvent = LoggingEvent.AddQuestion,
                                    question = event.question,
                                    questionSession = null,
                                    parentUser = parentUserState.value!!,
                                    profile = _currentChildProfile.value!!
                                )
                            }


                        }
                    }
                }
            }

            // todo new chat bug - change profiles strange behaviour()
            is ChatEvent.SelectChat -> {
                // TODO clear new chat state as soon as you select a chat
                _newChatSession.value = null

                _currentChatState.value = event.chatSession
                //println("current chat is ${_currentChatState.value?._id} new chat state is ${globalState._newChatState.value?._id}")
                getQuestionSessionsWithQuestionAndAnswers()
                getQuestionSessionsWithQuestionAndAnswersGroupedByDate()

                parentUserState.value?.let { parentUser ->
                    eventLogger.logChatEvent(
                        loggingEvent = LoggingEvent.LoadChat,
                        chatSession = event.chatSession,
                        parentUser = parentUser,
                        profile = _currentChildProfile.value
                    )
                }

                //todo why do i need to call this each time
                // todo for new chat not reflecting the change of current chat
            }

            is ChatEvent.SelectProfile -> {
                // globalState.updateCurrentChildProfile(childProfile = event.childProfile)
            }

            is ChatEvent.SelectSubtopic -> {
                _currentSubtopic.value = event.subtopic
                getSubtopicQuestions()

                parentUserState.value?.let { parentUser ->
                    eventLogger.logSubtopicEvent(
                        loggingEvent = LoggingEvent.SubtopicView,
                        chatSession = _currentChatState.value,
                        subtopic = event.subtopic,
                        parentUser = parentUser,
                        profile = _currentChildProfile.value
                    )

                }
            }

            is ChatEvent.SelectTopic -> {
                _currentTopic.value = event.topic
                getSubtopics()
            }

            is ChatEvent.UpdateSearchQueryText -> {
                _typedQueryText.value = event.searchQueryText
                // println(_typedQueryText.value)
            }

            is ChatEvent.ChangeLanguage -> {
                _currentLanguageCode.value = event.language
                appPreferences.setDefaultLanguage(event.language)

                getCurrentLanguage()

                parentUserState.value?.let { parentUser ->
                    eventLogger.logChangeLanguageEvent(
                        language = event.language,
                        parentUser = parentUser,
                        loggingEvent = LoggingEvent.ChangeLanguage,
                        profile = _currentChildProfile.value
                    )
                }
            }
        }
    }

    private fun getSubtopicQuestions() {
        getSubtopicQuestionsJob?.cancel()

        getSubtopicQuestionsJob = viewModelScope.launch {
            _currentSubtopic.onEach {
                _currentSubtopic.value?.let {
                    it.code?.let { subtopicCode ->
                        questionUseCases.getQuestionBySubtopic(subtopicCode)
                            ?.onEach { questionsList ->
                                _subtopicQuestionsListState.value = questionsList
                            }?.collect()
                    }
                }
            }.collect()
        }
    }

    private fun getAllQuestions() {
        getAllQuestionsJob?.cancel()

        getAllQuestionsJob = viewModelScope.launch {
            questionUseCases.getAllQuestions()?.onEach {
                _allQuestionsListState.value = it
                // println(_allQuestionsListState.value)
            }?.collect()
        }
    }

    private fun getChatQuestionSessions() {
        getChatQuestionSessionsJob?.cancel()

        getChatQuestionSessionsJob = viewModelScope.launch {
            // on each happens but what if it happens before
            _currentChatState.collect {
                // println("the current chat is ${_currentChatState.value?._id}")
                it?.let { chatSession ->
                    questionSessionUseCases.getChatQuestionSessions(chatSession._id)
                        ?.onEach { questionSessionList ->
                            _questionSessionListState.value = questionSessionList

                            // println("c${_currentChatState.value?._id}: ${_questionSessionListState.value}")
                        }?.collect()
                }
            }
        }
    }

    private fun getChatQuestionSessionsGroupedByDate() {
        getChatQuestionSessionsGroupedByDateJob?.cancel()

        getChatQuestionSessionsGroupedByDateJob = viewModelScope.launch {
            // on each happens but what if it happens before
            _currentChatState.collect {
                // println("the current chat is ${_currentChatState.value?._id}")
                it?.let { chatSession ->
                    questionSessionUseCases.getChatQuestionSessions(chatSession._id)
                        ?.onEach { questionSessionList ->
                            _questionSessionListGroupedByDateState.value =
                                questionSessionList.groupBy {
                                    RealmInstantConverter.toLocalDate(
                                        it.timeAsked
                                    )
                                }


                            // println("c${_currentChatState.value?._id}: ${_questionSessionListState.value}")
                        }?.collect()
                }
            }
        }
    }


    private fun getChildProfiles() {

        getChildProfilesJob?.cancel()

        getChildProfilesJob = viewModelScope.launch {
            globalState.parentUserState.onEach { parentUser ->
                parentUser?.let {
                    childProfileUseCases.getChildProfilesByParentUser(parentUser._id)
                        ?.onEach { childProfilesList ->
                            _childProfilesListState.value = childProfilesList
                        }?.collect()
                }

            }.collect()

        }
    }

    private fun getQuestionsWithAnswers() {
        getQuestionsWithAnswersJob?.cancel()

        getQuestionsWithAnswersJob = viewModelScope.launch {
            questionUseCases.getQuestionsWithAnswers()?.collect { questionsWithAnswersList ->
                // _state.value = state.value.copy(questionsWithAnswers = questionsWithAnswersList)
                _questionsWithAnswersListState.value = questionsWithAnswersList
            }
        }

    }

    // debug this
    private fun getQuestionSessionsWithQuestionAndAnswers() {
        getQuestionSessionWithQuestionAndAnswersJob?.cancel()
        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var questionSessionWithQuestionAndAnswersList: List<Triple<QuestionSession, Question?, List<Answer>?>?>

        getQuestionSessionWithQuestionAndAnswersJob = viewModelScope.launch {

            _currentChatState.onEach {
                // println("the current chat is ${_currentChatState.value?._id}")
                it?.let {
                    questionSessionUseCases.getChatQuestionSessions(chatSessionId = it._id)
                        ?.onEach { questionSessionList ->
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
                            _questionSessionsWithQuestionAndAnswersListState.value =
                                questionSessionWithQuestionAndAnswersList


                        }?.collect()
                }

            }.collect()

        }
    }

    private fun getQuestionSessionsWithQuestionAndAnswersGroupedByDate() {
        getQuestionSessionWithQuestionAndAnswersGroupedByDateJob?.cancel()
        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var relatedQuestionsList: List<Question?>? = null
        var questionSessionWithQuestionAndAnswersListGroupedByDate: Map<LocalDate, List<Triple<QuestionSession, Pair<Question?, List<Question?>?>, List<Answer>?>?>>

        getQuestionSessionWithQuestionAndAnswersGroupedByDateJob = viewModelScope.launch {

            _currentChatState.onEach {
                // println("the current chat is ${_currentChatState.value?._id}")
                it?.let {
                    questionSessionUseCases.getChatQuestionSessions(chatSessionId = it._id)
                        ?.onEach { questionSessionList ->
                            val questionSessionListGroupedByDate =
                                questionSessionList.groupBy { RealmInstantConverter.toLocalDate(it.timeAsked) }

                            questionSessionWithQuestionAndAnswersListGroupedByDate =
                                questionSessionListGroupedByDate.mapValues {
                                    it.value.map { questionSession ->

                                        questionSession.question?.let { questionId ->
                                            questionWithAnswer =
                                                questionUseCases.getQuestionWithAnswers(questionId)

                                            val answerThreadCode =
                                                questionWithAnswer?.first?.answerThread

                                            answerThreadCode?.let {
                                                answerThreadCode.let { code ->
                                                    val answerThread =
                                                        answerThreadUseCases.getAnswerThreadByCode(
                                                            code
                                                        )
                                                    println(answerThread?.relatedAnswerThreads?.size)


                                                    relatedQuestionsList =
                                                        answerThread?.relatedAnswerThreads?.map { answerThreadCode ->
                                                            answerThreadUseCases.getAnswerThreadByCode(
                                                                answerThreadCode
                                                            )?.let {

                                                                it.defaultQuestionCode?.let { questionCode ->
                                                                    questionUseCases.getQuestionByCode(
                                                                        questionCode
                                                                    )
                                                                }

                                                            }

                                                        }


                                                }

                                            }

                                        }





                                        Triple(
                                            questionSession,
                                            Pair(questionWithAnswer?.first, relatedQuestionsList),
                                            questionWithAnswer?.second
                                        )
                                    }

                                }

                            _questionSessionsWithQuestionAndAnswersListGroupedByDateState.value =
                                questionSessionWithQuestionAndAnswersListGroupedByDate


                        }?.collect()
                }

            }.collect()

        }
    }

    private fun getRelatedQuestionsList(answerThreadCode: String): List<Question?>? {

        var relatedQuestionsList: List<Question?>? = null

        viewModelScope.launch {
            answerThreadCode.let { code ->
                val answerThread =
                    answerThreadUseCases.getAnswerThreadByCode(code)
                println(answerThread?.relatedAnswerThreads?.size)

                relatedQuestionsList =
                    answerThread?.relatedAnswerThreads?.map { answerThreadCode ->
                        answerThreadUseCases.getAnswerThreadByCode(
                            answerThreadCode
                        )?.let {

                            it.defaultQuestionCode?.let { questionCode ->
                                questionUseCases.getQuestionByCode(questionCode)
                            }

                        }

                    }


            }


        }

        return relatedQuestionsList
    }

    private fun getTopics() {
        getTopicsJob?.cancel()

        getTopicsJob = viewModelScope.launch {
            topicUseCases.getAllTopics()?.onEach { topicsList ->
                // _state.value = state.value.copy(topicsList = topicsList)
                _topicsListState.value = topicsList
                // println(topicsList)
                if (topicsList.isNotEmpty()) {
                    _currentTopic.value = topicsList[0]
                    getSubtopics()
                }


            }?.collect()
        }
    }

    private fun getSubtopics() {
        getSubtopicsJob?.cancel()

        getSubtopicsJob = viewModelScope.launch {
            _currentTopic.onEach {
                _currentTopic.value?.let {
                    subtopicUseCases.getSubtopicsByTopic(it._id)?.onEach { subtopicsList ->
                        _subtopicsListState.value = subtopicsList
                    }?.collect()
                }
            }.collect()

        }
    }
}