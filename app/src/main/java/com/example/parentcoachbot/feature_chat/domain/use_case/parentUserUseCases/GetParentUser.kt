package com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository
import kotlinx.coroutines.flow.Flow

class GetParentUser(private val parentUserRepository: ParentUserRepository) {
    operator fun invoke(): Flow<ParentUser?>? {
        return parentUserRepository.getParentUser()
    }
}