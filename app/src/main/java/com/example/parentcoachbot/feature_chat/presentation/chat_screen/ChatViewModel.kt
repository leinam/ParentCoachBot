package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSearcher
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.AnswerUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.SubtopicUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO SEPARATE CHAT STATES
@OptIn(FlowPreview::class)
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val questionUseCases: QuestionUseCases,
    private val questionSessionUseCases: QuestionSessionUseCases,
    private val topicUseCases: TopicUseCases,
    private val subtopicUseCases: SubtopicUseCases,
    private val childProfileUseCases: ChildProfileUseCases,
    private val parentUserUseCases: ParentUserUseCases,
    private val answerUseCases: AnswerUseCases,
    private val chatSessionUseCases: ChatSessionUseCases,
    private val globalState: GlobalState,
    private val questionSearcher: QuestionSearcher
) : ViewModel() {
    private val _topicsListState = MutableStateFlow<List<Topic>>(emptyList())
    private val _currentChatState = MutableStateFlow<ChatSession?>(null)
    private val _childProfilesListState = MutableStateFlow<List<ChildProfile>>(emptyList())
    private val _subtopicsListState = MutableStateFlow<List<Subtopic>>(emptyList())
    private val _questionSessionListState = MutableStateFlow<List<QuestionSession>>(emptyList())
    private val _subtopicQuestionsListState = MutableStateFlow<List<Question>>(emptyList())
    private val _questionsWithAnswersListState =
        MutableStateFlow<MutableList<Pair<Question, List<Answer>>?>>(mutableListOf())
    private val _questionSessionsWithQuestionAndAnswersListState: MutableStateFlow<List<Triple<QuestionSession, Question?, List<Answer>?>?>> =
        MutableStateFlow(emptyList())
    private val _currentTopic: MutableStateFlow<Topic?> = MutableStateFlow(null)
    private val _currentSubtopic: MutableStateFlow<Subtopic?> = MutableStateFlow(null)
    private val _typedQueryText = MutableStateFlow("")
    private val _newChatSession = globalState._newChatState
    private val _searchResultsQuestionsListState = MutableStateFlow<List<Question>>(emptyList())
    private val _allQuestionsListState = MutableStateFlow<List<Question>>(emptyList())

    private val _chatViewModelState = mutableStateOf(
        ChatStateWrapper(
            subtopicQuestionsListState = _subtopicQuestionsListState,
            topicsListState = _topicsListState,
            questionsWithAnswersState = _questionsWithAnswersListState,
            questionSessionListState = _questionSessionListState,
            subtopicsListState = _subtopicsListState,
            childProfilesListState = _childProfilesListState,
            questionSessionsWithQuestionAndAnswersState = _questionSessionsWithQuestionAndAnswersListState,
            searchResultsQuestionsListState = _searchResultsQuestionsListState,
            currentTopicState = _currentTopic
        )
    )

    val chatViewModelState: State<ChatStateWrapper> = _chatViewModelState

    private var lastDeletedQuestion: Question? = null
    private var getSubtopicQuestionsJob: Job? = null // track job coroutine observing db
    private var getTopicsJob: Job? = null // track job coroutine observing db
    private var getSubtopicsJob: Job? = null // track job coroutine observing db
    private var getQuestionsWithAnswersJob: Job? = null // track job coroutine observing db
    private var getChatQuestionSessionsJob: Job? = null
    private var getChildProfilesJob: Job? = null
    private var getAllQuestionsJob: Job? = null

    init {
        getChatQuestionSessions()
        getTopics()
        getAllQuestions()
        getQuestionsWithAnswers()
        getChildProfiles()
        populateQuestionIndex()
        listenForNewChat()
        listenForChatQuery()
    }

    private fun listenForNewChat() {
        viewModelScope.launch {
            _newChatSession.onEach {
                println("new ${_newChatSession.value?._id}")
                _currentChatState.value = it
                getQuestionSessionWithQuestionAndAnswers()
            }.collect()
        }
    }

    private fun listenForChatQuery() {
        viewModelScope.launch {
            _typedQueryText.debounce(1500).onEach {
                    if (it.isNotBlank()) {
                        searchQuestions(it)
                    }
                }.collect()
        }
    }

    private fun populateQuestionIndex() {
        println("Populating Question Index")
        viewModelScope.launch {
            _allQuestionsListState.onEach {
                questionSearcher.populateIndex(it)
            }.collect()
        }
    }

    private fun searchQuestions(queryText: String) {
        val searchResult = questionSearcher.search(queryText = queryText)
        println("Search result for query: $queryText is $searchResult")
        viewModelScope.launch {
            _searchResultsQuestionsListState.value =
                questionUseCases.getQuestionsFromIdList(searchResult)
        }


    }

    // todo new chat doesn't always open
    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SaveQuestionSession -> {
                viewModelScope.launch {


                }
            }

            is ChatEvent.DeleteQuestionSession -> {
                viewModelScope.launch {
                    lastDeletedQuestion = event.question
                    questionUseCases.deleteQuestion(event.question)

                }
            }

            is ChatEvent.RestoreQuestion -> {
                viewModelScope.launch {
                    questionUseCases.addQuestion(question = lastDeletedQuestion ?: return@launch)
                    lastDeletedQuestion = null
                }
            }

            is ChatEvent.AddQuestionSession -> {
                viewModelScope.launch {
                    _currentChatState.value?.let { chatSession ->
                        questionSessionUseCases.newQuestionSession(
                            chatSessionId = chatSession._id, question = event.question
                        ).also {
                            event.question.answerThread?.let { answerThreadCode ->
                                answerUseCases.getAnswerThreadLastAnswer(answerThreadCode = answerThreadCode)
                                    ?.let { answerText ->
                                        chatSessionUseCases.updateChatLastAnswerText(
                                            answerText, chatSessionId = chatSession._id
                                        )
                                    }
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
                println("current chat is ${_currentChatState.value?._id} new chat state is ${globalState._newChatState.value?._id}")
                getQuestionSessionWithQuestionAndAnswers()

                //todo why do i need to call this each time
                // todo for new chat not reflecting the change of current chat
            }

            is ChatEvent.SelectProfile -> {
                // globalState.updateCurrentChildProfile(childProfile = event.childProfile)
            }

            is ChatEvent.SelectSubtopic -> {
                _currentSubtopic.value = event.subtopic
                getSubtopicQuestions()
            }

            is ChatEvent.SelectTopic -> {
                _currentTopic.value = event.topic
                getSubtopics()
            }

            is ChatEvent.UpdateSearchQueryText -> {
                _typedQueryText.value = event.searchQueryText
                println(_typedQueryText.value)
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
                            .onEach { questionsList ->
                                _subtopicQuestionsListState.value = questionsList
                            }.collect()
                    }
                }
            }.collect()
        }
    }

    private fun getAllQuestions() {
        getAllQuestionsJob?.cancel()

        getAllQuestionsJob = viewModelScope.launch {
            questionUseCases.getAllQuestions().onEach {
                _allQuestionsListState.value = it
                println(_allQuestionsListState.value)
            }.collect()
        }
    }

    private fun getChatQuestionSessions() {
        getChatQuestionSessionsJob?.cancel()

        getChatQuestionSessionsJob = viewModelScope.launch {
            // on each happens but what if it happens before
            _currentChatState.collect {
                println("the current chat is ${_currentChatState.value?._id}")
                it?.let { chatSession ->
                    questionSessionUseCases.getChatQuestionSessions(chatSession._id)
                        .onEach { questionSessionList ->
                            _questionSessionListState.value = questionSessionList
                            println("c${_currentChatState.value?._id}: ${_questionSessionListState.value}")
                        }.collect()
                }
            }
        }
    }


    private fun getChildProfiles() {
        viewModelScope.launch {
            val parentUser = parentUserUseCases.getParentUser()
            getChildProfilesJob?.cancel()

            getChildProfilesJob = viewModelScope.launch {
                parentUser?.let {
                    childProfileUseCases.getChildProfilesByParentUser(it._id)
                        .onEach { childProfilesList ->
                            _childProfilesListState.value = childProfilesList
                        }.collect()
                }
            }
        }
    }

    private fun getQuestionsWithAnswers() {
        getQuestionsWithAnswersJob?.cancel()

        getQuestionsWithAnswersJob = viewModelScope.launch {
            questionUseCases.getQuestionsWithAnswers().collect { questionsWithAnswersList ->
                // _state.value = state.value.copy(questionsWithAnswers = questionsWithAnswersList)
                _questionsWithAnswersListState.value = questionsWithAnswersList
            }
        }

    }

    // debug this
    private fun getQuestionSessionWithQuestionAndAnswers() {
        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var questionSessionWithQuestionAndAnswersList: List<Triple<QuestionSession, Question?, List<Answer>?>?>

        viewModelScope.launch {

            _currentChatState.onEach {
                println("the current chat is ${_currentChatState.value?._id}")
                it?.let {
                    questionSessionUseCases.getChatQuestionSessions(chatSessionId = it._id)
                        .onEach { questionSessionList ->
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
                        }.collect()
                }

            }.collect()

        }
    }

    private fun getTopics() {
        getTopicsJob?.cancel()

        getTopicsJob = viewModelScope.launch {
            topicUseCases.getAllTopics().onEach { topicsList ->
                // _state.value = state.value.copy(topicsList = topicsList)
                _topicsListState.value = topicsList
                println(topicsList)
                if(topicsList.isNotEmpty()){
                    _currentTopic.value = topicsList[0]
                    getSubtopics()
                }


            }.collect()
        }
    }

    private fun getSubtopics() {
        getSubtopicsJob?.cancel()

        getSubtopicsJob = viewModelScope.launch {
            _currentTopic.onEach {
                _currentTopic.value?.let {
                    subtopicUseCases.getSubtopicsByTopic(it._id).onEach { subtopicsList ->
                        _subtopicsListState.value = subtopicsList
                    }.collect()
                }
            }.collect()

        }
    }
}