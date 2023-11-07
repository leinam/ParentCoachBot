package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository

class GetChildProfileTest(private val childProfileRepository: ChildProfileRepository) {
    suspend operator fun invoke(parentUserId: String): ChildProfile? {
        return childProfileRepository.getChildProfileByParentTest(parentUserId)
    }

}