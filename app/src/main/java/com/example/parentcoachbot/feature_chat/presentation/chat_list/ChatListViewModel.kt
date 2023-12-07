package com.example.parentcoachbot.feature_chat.presentation.chat_list

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val application: Application,
    private val chatSessionUseCases: ChatSessionUseCases,
    private val topicUseCases: TopicUseCases,
    private val globalState: GlobalState,
    private val authManager: AuthManager
) : ViewModel() {

    private val _chatSessionsListState = MutableStateFlow<List<ChatSession>>(emptyList())
    private val _topicsListState = MutableStateFlow<List<Topic>>(emptyList())
    private val _newChatState = globalState.newChatState
    private val childProfileListState = globalState.childProfilesListState
    private val _currentChildProfile = globalState.currentChildProfileState
    private val parentUserState = globalState.parentUserState
    private val _currentLanguageCode = globalState.currentLanguageCode

    private var getChildProfileChatSessionsJob: Job? = null

    private val _chatListStateWrapper = mutableStateOf(
        ChatListStateWrapper(
            chatSessionListState = _chatSessionsListState,
            topicsListState = _topicsListState,
            childProfileListState = childProfileListState,
            newChatState = _newChatState,
            currentChildProfile = globalState.currentChildProfileState,
            currentLanguageCode = _currentLanguageCode
        )
    )


    var chatListViewModelState: State<ChatListStateWrapper> = _chatListStateWrapper
    private fun getCurrentLanguage() {
        viewModelScope.launch {

        }
    }

    init {
        getProfileChatSessions()
        // getCurrentLanguage()
    }

    fun onEvent(chatListEvent: ChatListEvent) {
        when (chatListEvent) {
            is ChatListEvent.DeleteChat -> {
                viewModelScope.launch {
                    chatSessionUseCases.deleteChatSession(chatListEvent.chatSession._id)
                }
            }

            ChatListEvent.NewChat -> {
                viewModelScope.launch {
                    _currentChildProfile.value?.let { currentChildProfile ->

                        ChatSession().apply {
                            this.childProfile = currentChildProfile._id
                            this._partition = authManager.authenticatedRealmUser.value?.id
                        }.let {
                            _newChatState.value = it

                            _newChatState.value?.let { chatSession ->
                                chatSessionUseCases.newChatSession(chatSession)
                            }
                        }
                    }
                }
            }


            is ChatListEvent.PinChat -> {
                viewModelScope.launch {
                    chatSessionUseCases.togglePinChatSession(chatListEvent.chatSession._id)
                }
            }

            is ChatListEvent.UpdateChildProfile -> {
                getProfileChatSessions()
                // have to call this every time or the list isn't current
            }


        }

    }

    private fun getProfileChatSessions() {
        getChildProfileChatSessionsJob?.cancel()
        // every time a chat session changes entire updated list is emitted and

        getChildProfileChatSessionsJob = viewModelScope.launch {

            _currentChildProfile.onEach { childProfile ->
                println(childProfile?.name)
                childProfile?.let {
                    chatSessionUseCases.getProfileChatSessions(it._id)?.onEach { chatSessionList ->
                        // on each emission we set the state again
                        _chatSessionsListState.value = chatSessionList
                    }?.collect()
                }
            }.collect()
        }
    }


    private fun getTopicsList() {
        viewModelScope.launch {
            topicUseCases.getAllTopics()?.onEach {
                _topicsListState.value = it
            }
        }
    }
}

