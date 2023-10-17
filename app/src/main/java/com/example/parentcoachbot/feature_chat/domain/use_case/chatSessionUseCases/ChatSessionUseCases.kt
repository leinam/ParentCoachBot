package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

data class ChatSessionUseCases(val getProfileChatSessions: GetChatSessionsByChildProfile,
                               val getChatSessionById: GetChatSessionById,
                               val newChatSession: NewChatSession,
                               val togglePinChatSession: TogglePinChatSession,
                               val deleteChatSession: DeleteChatSession,
                               val updateChatLastAnswerText: UpdateChatLastAnswerText,
                               val updateChatTitle: UpdateChatTitle
)
