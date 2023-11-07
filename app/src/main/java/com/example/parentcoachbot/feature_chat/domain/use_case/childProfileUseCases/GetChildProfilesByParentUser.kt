package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import kotlinx.coroutines.flow.Flow

class GetChildProfilesByParentUser (private val childProfileRepository: ChildProfileRepository){
    suspend operator fun invoke(parentUserId: String): Flow<List<ChildProfile>> {
        return childProfileRepository.getChildProfileByParent(parentUserId)
    }
}