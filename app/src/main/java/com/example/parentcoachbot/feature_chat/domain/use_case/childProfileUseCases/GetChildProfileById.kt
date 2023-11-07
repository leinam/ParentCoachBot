package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository

class GetChildProfileById(private val childProfileRepository: ChildProfileRepository) {

    suspend operator fun invoke(childProfileId: String): ChildProfile? {
        return childProfileRepository.getChildProfileById(childProfileId)
    }
}