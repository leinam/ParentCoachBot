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
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
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
import kotlinx.coroutines.flow.StateFlow
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
    private val globalState: GlobalState) : ViewModel()

{

    private val _topicsListState = MutableStateFlow<List<Topic>>(emptyList())
    private val _currentChatState = MutableStateFlow<ChatSession?>(null)
    private val _childProfilesListState = MutableStateFlow<List<ChildProfile>>(emptyList())
    private val _subtopicsListState = MutableStateFlow<List<Subtopic>>(emptyList())
    private val _questionSessionListState = MutableStateFlow<List<QuestionSession>>(emptyList())
    private val _subtopicQuestionsListState = MutableStateFlow<List<Question>>(emptyList())
    private val _questionsWithAnswersListState = MutableStateFlow<MutableList<Pair<Question,
            List<Answer>>?>>(mutableListOf())
    private val _questionSessionsWithQuestionAndAnswersListState: MutableStateFlow<List<Triple<QuestionSession,
            Question?, List<Answer>?>?>> = MutableStateFlow(emptyList())
    private val _currentTopic: MutableStateFlow<Topic?> = MutableStateFlow(null)
    private val _currentSubtopic: MutableStateFlow<Subtopic?> = MutableStateFlow(null)
    private val _typedQueryText = MutableStateFlow("")
    val searchQueryText: StateFlow<String> = MutableStateFlow("")



    private val _chatViewModelState = mutableStateOf(ChatStateWrapper(
        subtopicQuestionsListState = _subtopicQuestionsListState,
        topicsListState = _topicsListState,
        questionsWithAnswersState = _questionsWithAnswersListState,
        questionSessionListState = _questionSessionListState,
        subtopicsListState = _subtopicsListState,
        childProfilesListState = _childProfilesListState,
        questionSessionsWithQuestionAndAnswersState = _questionSessionsWithQuestionAndAnswersListState
    ))

    val chatViewModelState: State<ChatStateWrapper> = _chatViewModelState

    private var lastDeletedQuestion: Question? = null
    private var getQuestionsJob: Job? = null // track job coroutine observing db
    private var getTopicsJob: Job? = null // track job coroutine observing db
    private var getSubtopicsJob: Job? = null // track job coroutine observing db
    private var getQuestionsWithAnswersJob: Job? = null // track job coroutine observing db
    private var getChatQuestionSessionsJob: Job? = null
    private var getChildProfilesJob: Job? = null

    init {
        getTopics()
        getQuestionsWithAnswers()
        getChildProfiles()

        viewModelScope.launch {
            _typedQueryText.debounce(2000)
                .collect{
                    // run search
                println("the text is $it")
            }


        }
    }

    // todo new chat doesn't always open
    fun onEvent(event: ChatEvent){
            when (event){
                is ChatEvent.FavouriteQuestionSession -> {}

                is ChatEvent.DeleteQuestionSession -> {
                    viewModelScope.launch {
                        lastDeletedQuestion = event.question
                        questionUseCases.deleteQuestion(event.question)

                    }
                }

                is ChatEvent.RestoreQuestion -> {
                    viewModelScope.launch{
                        questionUseCases.addQuestion(question = lastDeletedQuestion ?: return@launch)
                        lastDeletedQuestion = null
                    }
                }

                is ChatEvent.AddQuestionSession -> {
                    viewModelScope.launch {
                                    _currentChatState.value?.let {
                                        questionSessionUseCases.newQuestionSession(
                                            chatSessionId = it._id,
                                            question = event.question)
                                    }
                                }
                }

                // todo new chat bug - change profiles strange behaviour()
                is ChatEvent.SelectChat -> {
                    _currentChatState.value = event.chatSession
                    println("current chat is ${_currentChatState.value?._id}")
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

    private fun getSubtopicQuestions(){
        getQuestionsJob?.cancel()

        getQuestionsJob = viewModelScope.launch {
            _currentSubtopic.onEach {
                _currentSubtopic.value?.let {
                    questionUseCases.getQuestionBySubtopic(it._id).onEach {
                            questionsList ->
                        _subtopicQuestionsListState.value = questionsList
                    }.collect()
                }
            }.collect()
        }
    }

    private fun getChatQuestionSessions(){
        getChatQuestionSessionsJob?.cancel()

        getChatQuestionSessionsJob = viewModelScope.launch{
            // on each happens but what if it happens before
            _currentChatState.collect {
                println("the current chat is ${_currentChatState.value?._id}")
                it?.let { chatSession ->
                    questionSessionUseCases.getChatQuestionSessions(chatSession._id).onEach {
                            questionSessionList ->
                        _questionSessionListState.value = questionSessionList
                        println("c${_currentChatState.value?._id}: ${_questionSessionListState.value}")
                    }.collect()
                }
            }
            }
        }


    private fun getChildProfiles(){
        viewModelScope.launch {
            val parentUser = parentUserUseCases.getParentUser()
            getChildProfilesJob?.cancel()

            getChildProfilesJob = viewModelScope.launch {
                childProfileUseCases.getChildProfilesByParentUser(parentUser._id).onEach {
                    childProfilesList ->
                    _childProfilesListState.value = childProfilesList
                }.collect()
            }
        }
    }

    private fun getQuestionsWithAnswers(){
        getQuestionsWithAnswersJob?.cancel()

        getQuestionsWithAnswersJob = viewModelScope.launch {
            questionUseCases.getQuestionsWithAnswers().collect {
                    questionsWithAnswersList ->
                // _state.value = state.value.copy(questionsWithAnswers = questionsWithAnswersList)
                _questionsWithAnswersListState.value = questionsWithAnswersList
            }
        }

    }

    // debug this
    private fun getQuestionSessionWithQuestionAndAnswers(){
        var questionWithAnswer: Pair<Question, List<Answer>>? = null
        var questionSessionWithQuestionAndAnswersList: List<Triple<QuestionSession, Question?, List<Answer>?>?>

        viewModelScope.launch{

            _currentChatState.onEach {
                println("the current chat is ${_currentChatState.value?._id}")
                it?.let {
                    questionSessionUseCases.getChatQuestionSessions(chatSessionId = it._id).onEach {
                        questionSessionList ->
                        questionSessionWithQuestionAndAnswersList = questionSessionList.map {
                            questionSession ->

                            questionSession.question?.let { questionId ->
                                questionWithAnswer = questionUseCases.getQuestionWithAnswers(questionId)
                            }

                            Triple(questionSession, questionWithAnswer?.first, questionWithAnswer?.second)
                        }
                        _questionSessionsWithQuestionAndAnswersListState.value = questionSessionWithQuestionAndAnswersList
                    }.collect()
                }

            }.collect()

    }
    }

    private fun getTopics(){
        getTopicsJob?.cancel()

        getTopicsJob = viewModelScope.launch {
            topicUseCases.getAllTopics().onEach {
                    topicsList ->
                // _state.value = state.value.copy(topicsList = topicsList)
                _topicsListState.value = topicsList

            }.collect()
        }
    }

    private fun getSubtopics(){
        getSubtopicsJob?.cancel()

        getSubtopicsJob = viewModelScope.launch {
            _currentTopic.onEach {
                _currentTopic.value?.let {
                    subtopicUseCases.getSubtopicsByTopic(it._id).onEach {
                            subtopicsList ->
                        _subtopicsListState.value = subtopicsList
                    }.collect()
                }
            }.collect()

        }
    }
}