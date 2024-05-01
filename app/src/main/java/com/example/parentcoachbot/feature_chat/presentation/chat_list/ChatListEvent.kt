package com.example.parentcoachbot.feature_chat.presentation.chat_list

import androidx.annotation.DrawableRes
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession

sealed class ChatListEvent {
    object NewChat : ChatListEvent()
    data class DeleteChat(val chatSession: ChatSession) : ChatListEvent()
    data class PinChat(val chatSession: ChatSession) : ChatListEvent()
    object UpdateChildProfile : ChatListEvent()
    data class SelectPDFResource(val fileName: String) : ChatListEvent()
    data class SelectImageResource(@DrawableRes val imageId: Int) : ChatListEvent()

}
