package com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases

data class ChildProfileUseCases(val getChildProfileById: GetChildProfileById,
                                val getChildProfilesByParentUser: GetChildProfilesByParentUser,
                                val getChildProfileTest: GetChildProfileTest,
                                val newChildProfile: NewChildProfile)
