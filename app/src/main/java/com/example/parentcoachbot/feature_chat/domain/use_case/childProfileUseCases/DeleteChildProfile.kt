package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import kotlinx.coroutines.flow.onEach

class DeleteChildProfile(
    private val childProfileRepository: ChildProfileRepository,
    private val chatSessionUseCases: ChatSessionUseCases
) {
    suspend operator fun invoke(childProfile: ChildProfile) {

        val profileChatSessions =
            chatSessionUseCases.getProfileChatSessions(childProfileId = childProfile._id)

        profileChatSessions?.onEach { chatSessionsList ->
            chatSessionsList.forEach { chatSession ->
                (chatSessionUseCases.deleteChatSession(chatSessionId = chatSession._id))
            }
        }

        childProfileRepository.deleteChildProfile(childProfile._id)
    }
}