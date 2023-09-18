package com.example.parentcoachbot.common

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow

class GlobalState(val parentUserUseCases: ParentUserUseCases,
                  val childProfileUseCases: ChildProfileUseCases) {

    val parentUserState = MutableStateFlow<ParentUser?>(null)
    val _childProfilesListState = MutableStateFlow<List<ChildProfile>>(emptyList())
    val _currentChildProfileState = MutableStateFlow<ChildProfile?>(null)
    val _newChatState = MutableStateFlow<ChatSession?>(null)


    var getChildProfilesJob: Job? = null

    init {
        getParentUser()
    }

    private fun getParentUser() {
        parentUserState.value = parentUserUseCases.getParentUser()
        println("The global parent state is ${parentUserState.value}")
    }

    fun updateCurrentChildProfile(childProfile: ChildProfile){
        _currentChildProfileState.value = childProfile
    }

}