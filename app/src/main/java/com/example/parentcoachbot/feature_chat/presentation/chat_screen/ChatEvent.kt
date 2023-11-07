package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic

sealed class ChatEvent{
    data class AddQuestionSession(val question: Question): ChatEvent()
    data class DeleteQuestionSession(val questionSession: QuestionSession) : ChatEvent()
    data class SaveQuestionSession(val questionSessionId: String): ChatEvent()
    data class SelectChat(val chatSession: ChatSession): ChatEvent()
    data class SelectTopic(val topic: Topic): ChatEvent()
    data class SelectSubtopic(val subtopic: Subtopic): ChatEvent()
    data class SelectProfile(val childProfile: ChildProfile): ChatEvent()
    data class ChangeLanguage(val language: String): ChatEvent()
    data class UpdateSearchQueryText(val searchQueryText: String): ChatEvent()
    object RestoreQuestion: ChatEvent()

}
