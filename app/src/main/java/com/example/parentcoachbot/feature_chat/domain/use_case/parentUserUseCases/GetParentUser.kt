package com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository

class GetParentUser(private val parentUserRepository: ParentUserRepository) {
    operator fun invoke(): ParentUser? {
        return parentUserRepository.getParentUser()
    }
}