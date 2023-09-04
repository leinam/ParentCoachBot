package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class GetChildProfilesByParentUser (private val childProfileRepository: ChildProfileRepository){
    suspend operator fun invoke(parentUserId: ObjectId): Flow<List<ChildProfile>> {
        return childProfileRepository.getChildProfileByParent(parentUserId)
    }
}