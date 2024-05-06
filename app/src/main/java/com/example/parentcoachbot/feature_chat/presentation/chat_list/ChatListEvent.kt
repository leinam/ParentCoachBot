package com.example.parentcoachbot.feature_chat.presentation.chat_list

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.presentation.resources_screens.ResourceItem

sealed class ChatListEvent {
    object NewChat : ChatListEvent()
    object TrimChats : ChatListEvent()
    data class DeleteChat(val chatSession: ChatSession) : ChatListEvent()
    data class PinChat(val chatSession: ChatSession) : ChatListEvent()
    object UpdateChildProfile : ChatListEvent()
    data class SelectPDFResource(val resourceItem: ResourceItem) : ChatListEvent()
    data class SelectImageResource( val resourceItem: ResourceItem) : ChatListEvent()

}
