package com.example.parentcoachbot.feature_chat.presentation.chat_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
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
    private val topicUseCases: TopicUseCases,
    private val globalState: GlobalState
) : ViewModel() {


    private val _chatSessionsListState = MutableStateFlow<List<ChatSession>>(emptyList())
    private val _topicsListState = MutableStateFlow<List<Topic>>(emptyList())
    private val _newChatState = globalState._newChatState
    private val childProfileListState = globalState._childProfilesListState
    private val _currentChildProfile = globalState._currentChildProfileState
    private val parentUserState = globalState.parentUserState

    private var getChildProfileChatSessionsJob: Job? = null

    private val _chatListStateWrapper = mutableStateOf(
        ChatListStateWrapper(
            chatSessionListState = _chatSessionsListState,
            topicsListState = _topicsListState,
            childProfileListState = childProfileListState,
            newChatState = _newChatState
        )
    )

    val chatListViewModelState: State<ChatListStateWrapper> = _chatListStateWrapper

    init {
        getProfileChatSessions()
    }

    fun onEvent(chatListEvent: ChatListEvent) {
        when (chatListEvent) {
            is ChatListEvent.DeleteChat -> {}

            ChatListEvent.NewChat -> {
                viewModelScope.launch {
                    _currentChildProfile.value?.let {
                            currentChildProfile ->

                        ChatSession().apply {
                            this.childProfile = currentChildProfile._id
                        }.let {
                            _newChatState.value = it

                            _newChatState.value?.let { chatSession ->
                                chatSessionUseCases.newChatSession(chatSession)
                            }
                        }
                    }
                }
            }

            is ChatListEvent.SelectChat -> {

            }

            is ChatListEvent.SelectProfile -> {
                getProfileChatSessions()
            }


        }

    }

    private fun getProfileChatSessions() {
        getChildProfileChatSessionsJob?.cancel()
        // every time a chat session changes entire updated list is emitted and

        getChildProfileChatSessionsJob = viewModelScope.launch {

            _currentChildProfile.onEach { childProfile ->
                println("the current child profile is ${childProfile?._id}")
                childProfile?.let {
                    chatSessionUseCases.getProfileChatSessions(it._id).onEach { chatSessionList ->
                        // on each emission we set the state again
                        _chatSessionsListState.value = chatSessionList
                    }.collect()
                }
            }.collect()


        }
    }


    private fun getTopicsList() {
        viewModelScope.launch {
            topicUseCases.getAllTopics().onEach {
                _topicsListState.value = it
            }
        }
    }
}

