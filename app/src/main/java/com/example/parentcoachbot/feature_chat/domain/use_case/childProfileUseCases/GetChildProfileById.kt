package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import org.mongodb.kbson.ObjectId

class GetChildProfileById(private val childProfileRepository: ChildProfileRepository) {

    suspend operator fun invoke(childProfileId: ObjectId): ChildProfile {
        return childProfileRepository.getChildProfileById(childProfileId)
    }
}