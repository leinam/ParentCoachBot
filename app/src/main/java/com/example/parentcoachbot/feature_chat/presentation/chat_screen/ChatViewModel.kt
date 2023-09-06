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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

// TODO SEPARATE CHAT STATES
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
    private val _questionsListState = MutableStateFlow<List<Question>>(emptyList())
    private val _questionsWithAnswersListState = MutableStateFlow<MutableList<Pair<Question,
            List<Answer>>?>>(mutableListOf())

    private val _chatViewModelState = mutableStateOf(ChatStateWrapper(
        questionsListState = _questionsListState,
        topicsListState = _topicsListState,
        questionsWithAnswersState = _questionsWithAnswersListState,
        questionSessionListState = _questionSessionListState,
        subtopicsListState = _subtopicsListState,
        childProfilesListState = _childProfilesListState
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
        getChatQuestionSessions()
        getTopics()
        getChildProfiles()
    }

    fun onEvent(event: ChatEvent){
            when (event){
                is ChatEvent.FavouriteQuestion -> {}

                is ChatEvent.DeleteQuestion -> {
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

                is ChatEvent.SelectQuestion -> TODO()

                is ChatEvent.SelectChat -> {
                    _currentChatState.value = event.chatSession
                    getChatQuestionSessions() //todo why do i need to call this each time

                }

                is ChatEvent.SelectProfile -> {
                    // globalState.updateCurrentChildProfile(childProfile = event.childProfile)
                }
            }
        }

    private fun getQuestions(){
        getQuestionsJob?.cancel()
        getQuestionsJob = viewModelScope.launch {
            questionUseCases.getAllQuestions().onEach {
                    questionsList ->
                _questionsListState.value = questionsList
            }.collect()
        }
    }

    private fun getChatQuestionSessions(){
        getChatQuestionSessionsJob?.cancel()

        getChatQuestionSessionsJob = viewModelScope.launch{
            _currentChatState.onEach {
                println("the current chat is ${_currentChatState.value}")
                it?.let {
                    questionSessionUseCases.getChatQuestionSessions(it._id).onEach {
                            questionSessionList ->
                        _questionSessionListState.value = questionSessionList

                        println(chatViewModelState.value.questionSessionListState.value)
                        chatViewModelState.value.questionSessionListState.value.forEach { println("the ID is $it._id") }

                    }.collect()
                }
            }.collect()

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

    private fun getTopics(){
        getTopicsJob?.cancel()

        getTopicsJob = viewModelScope.launch {
            topicUseCases.getAllTopics().onEach {
                    topicsList ->
                // _state.value = state.value.copy(topicsList = topicsList)
                _topicsListState.value = topicsList

                topicsList.forEach {
                    getSubtopics(topicId = it._id)
                }
            }.collect()
        }
    }

    private fun getSubtopics(topicId: ObjectId){
        getSubtopicsJob?.cancel()

        getSubtopicsJob = viewModelScope.launch {
            subtopicUseCases.getSubtopicsByTopic(topicId = topicId).onEach {
                     subtopicsList ->
                _subtopicsListState.value = subtopicsList
            }.collect()
        }
    }
}