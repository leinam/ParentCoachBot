package com.example.parentcoachbot.feature_chat.presentation.chat_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.common.LoggingEvent
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.presentation.resources_screens.ResourceItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatSessionUseCases: ChatSessionUseCases,
    private val questionSessionUseCases: QuestionSessionUseCases,
    private val globalState: GlobalState,
    private val authManager: AuthManager,
    private val eventLogger: EventLogger
) : ViewModel() {

    private val _chatSessionsListState = MutableStateFlow<List<ChatSession>>(emptyList())
    private val _topicsListState = MutableStateFlow<List<Topic>>(emptyList())
    private val _newChatState = globalState.newChatState
    private val childProfileListState = globalState.childProfilesListState
    private val _currentChildProfile = globalState.currentChildProfileState
    private val parentUserState = globalState.parentUserState
    private val _currentLanguageCode = globalState.currentLanguageCode
    private val _currentPDFResource: MutableStateFlow<ResourceItem?> = MutableStateFlow(null)
    private val _currentImageResource = MutableStateFlow<ResourceItem?>(null)
    private val _currentCountry = globalState.currentCountry
    private val _parentUserState = globalState.parentUserState

    private var getChildProfileChatSessionsJob: Job? = null
    private var getCurrentCountryJob: Job? = null
    private var getCurrentLanguageJob: Job? = null


    private val _chatListStateWrapper = mutableStateOf(
        ChatListStateWrapper(
            chatSessionListState = _chatSessionsListState,
            topicsListState = _topicsListState,
            childProfileListState = childProfileListState,
            newChatState = _newChatState,
            currentChildProfile = globalState.currentChildProfileState,
            currentLanguageCode = _currentLanguageCode,
            currentPdfResource = _currentPDFResource,
            currentImageResource = _currentImageResource,
            currentCountry = _currentCountry
        )
    )

    var chatListViewModelState: State<ChatListStateWrapper> = _chatListStateWrapper

    private fun getCountry() {
        getCurrentCountryJob?.cancel()

        getCurrentCountryJob = viewModelScope.launch {
            _parentUserState.onEach {
                println("country ${_currentCountry.value}")
            }.collect()
        }
    }

    private fun getCurrentLanguage() {
        getCurrentLanguageJob?.cancel()

        getCurrentLanguageJob = viewModelScope.launch {

        }
    }

    init {
        getProfileChatSessions()
        // getCountry()
        // getCurrentLanguage()
    }

    fun onEvent(chatListEvent: ChatListEvent) {
        when (chatListEvent) {
            is ChatListEvent.DeleteChat -> {
                viewModelScope.launch {
                    chatSessionUseCases.deleteChatSession(chatListEvent.chatSession._id)
                }

                parentUserState.value?.let { parentUser ->
                    eventLogger.logChatEvent(
                        loggingEvent = LoggingEvent.DeleteChat,
                        chatSession = chatListEvent.chatSession,
                        parentUser = parentUser,
                        profile = _currentChildProfile.value
                    )
                }
            }

            is ChatListEvent.SelectPDFResource -> {
                _currentPDFResource.value = chatListEvent.resourceItem

                parentUserState.value?.let {
                    eventLogger.logResourceEvent(
                        language = _currentLanguageCode.value,
                        loggingEvent = LoggingEvent.ViewResourceItem,
                        profile = _currentChildProfile.value,
                        resourceItem = chatListEvent.resourceItem,
                        parentUser = it
                    )
                }
            }

            is ChatListEvent.SelectImageResource -> {
                _currentImageResource.value = chatListEvent.resourceItem

                parentUserState.value?.let {
                    eventLogger.logResourceEvent(
                        language = _currentLanguageCode.value,
                        loggingEvent = LoggingEvent.ViewResourceItem,
                        profile = _currentChildProfile.value,
                        resourceItem = chatListEvent.resourceItem,
                        parentUser = it

                    )
                }
            }

            ChatListEvent.NewChat -> {
                viewModelScope.launch {
                    _currentChildProfile.value?.let { currentChildProfile ->

                        ChatSession().apply {
                            this.childProfile = currentChildProfile._id
                            this.parentUsername = parentUserState.value?.username
                            this.owner_id = authManager.authenticatedRealmUser.value?.id
                            this.parentUsername = parentUserState.value?.username
                        }.let {
                            _newChatState.value = it

                            _newChatState.value?.let { chatSession ->
                                chatSessionUseCases.newChatSession(chatSession)

                                parentUserState.value?.let { parentUser ->
                                    eventLogger.logChatEvent(
                                        loggingEvent = LoggingEvent.NewChat,
                                        chatSession = chatSession,
                                        parentUser = parentUser,
                                        profile = _currentChildProfile.value
                                    )
                                }

                            }
                        }
                    }
                }


            }


            is ChatListEvent.PinChat -> {
                viewModelScope.launch {
                    chatSessionUseCases.togglePinChatSession(chatListEvent.chatSession._id)
                }

                parentUserState.value?.let { parentUser ->
                    eventLogger.logChatEvent(
                        loggingEvent = LoggingEvent.PinChat,
                        chatSession = chatListEvent.chatSession,
                        parentUser = parentUser,
                        profile = _currentChildProfile.value
                    )
                }


            }

            is ChatListEvent.UpdateChildProfile -> {
                getProfileChatSessions()
                // have to call this every time or the list isn't current
            }

            is ChatListEvent.TrimChats -> {
                trimChats()
            }

        }

    }

    private fun trimChats() {
        viewModelScope.launch {
            _chatSessionsListState.value.forEach { chatSession ->
                questionSessionUseCases.getChatQuestionSessions(chatSessionId = chatSession._id)
                    ?.onEach {
                        if (it.isEmpty()) {
                            chatSessionUseCases.deleteChatSession(chatSession._id)
                        }
                    }?.collect()
            }
        }

    }

    private fun getProfileChatSessions() {
        getChildProfileChatSessionsJob?.cancel()
        // every time a chat session changes entire updated list is emitted and

        getChildProfileChatSessionsJob = viewModelScope.launch {

            _currentChildProfile.onEach { childProfile ->
                // println(childProfile?.name)
                childProfile?.let {
                    chatSessionUseCases.getProfileChatSessions(it._id)?.onEach { chatSessionList ->
                        // on each emission we set the state again
                        _chatSessionsListState.value = chatSessionList
                    }?.collect()
                }
            }.collect()
        }


    }


}

