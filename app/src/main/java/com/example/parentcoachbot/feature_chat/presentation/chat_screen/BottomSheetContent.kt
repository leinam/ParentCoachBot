package com.example.parentcoachbot.feature_chat.presentation.chat_screen

sealed class BottomSheetContent{
    object Topics: BottomSheetContent()
    object SubTopics: BottomSheetContent()
    object Questions: BottomSheetContent()
}
