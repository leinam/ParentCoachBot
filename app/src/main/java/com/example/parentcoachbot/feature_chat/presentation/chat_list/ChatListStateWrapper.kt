package com.example.parentcoachbot.feature_chat.presentation.chat_list

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ChatListStateWrapper(
    val chatSessionListState: StateFlow<List<ChatSession>> = MutableStateFlow(emptyList()),
    val topicsListState: StateFlow<List<Topic>> = MutableStateFlow(emptyList()),
    val currentChildProfile: StateFlow<ChildProfile?> = MutableStateFlow(null),
    val currentLanguageCode: StateFlow<String?> = MutableStateFlow(null),
    val childProfileListState: StateFlow<List<ChildProfile>> = MutableStateFlow(
        emptyList()
    ),
    val newChatState: StateFlow<ChatSession?> = MutableStateFlow(null)
)