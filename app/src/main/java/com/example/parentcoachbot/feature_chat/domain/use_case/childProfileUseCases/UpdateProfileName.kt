package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository

class UpdateProfileName(private val childProfileRepository: ChildProfileRepository) {

    suspend operator fun invoke(childProfile: ChildProfile, profileName: String): ChildProfile? {
        return childProfileRepository.updateProfileName(childProfile._id, profileName)
    }
}