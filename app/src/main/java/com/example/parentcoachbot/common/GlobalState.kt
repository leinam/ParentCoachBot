package com.example.parentcoachbot.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow

class GlobalState(val parentUserUseCases: ParentUserUseCases,
                  val childProfileUseCases: ChildProfileUseCases,
                  val application: Application) {

    val parentUserState = MutableStateFlow<ParentUser?>(null)
    val _childProfilesListState = MutableStateFlow<List<ChildProfile>>(emptyList())
    val _currentChildProfileState = MutableStateFlow<ChildProfile?>(null)
    val _newChatState = MutableStateFlow<ChatSession?>(null)

    val appPreferences: SharedPreferences = application.applicationContext.getSharedPreferences(
        "MyAppPreferences",
        Context.MODE_PRIVATE
    )

    val _currentLanguageCode = MutableStateFlow<String>("en")
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