package com.example.parentcoachbot.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.util.Language
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class GlobalState(private val parentUserUseCases: ParentUserUseCases,
                  private val childProfileUseCases: ChildProfileUseCases,
                  private val application: Application) {

    val parentUserState = MutableStateFlow<ParentUser?>(null)
    val childProfilesListState = MutableStateFlow<List<ChildProfile>>(emptyList())
    val currentChildProfileState = MutableStateFlow<ChildProfile?>(null)
    val newChatState = MutableStateFlow<ChatSession?>(null)
    val realmUserState = MutableStateFlow<User?>(null)

    val appPreferences: SharedPreferences = application.applicationContext.getSharedPreferences(
        "MyAppPreferences",
        Context.MODE_PRIVATE
    )

    val currentLanguageCode = MutableStateFlow<String>(Language.English.isoCode)

    init {

    }

    suspend fun getParentUser() {
        parentUserUseCases.getParentUser()?.onEach {
            parentUserState.value = it
            println("The global parent state is ${parentUserState.value?._id}")
        }?.collect()
    }

    fun updateCurrentChildProfile(childProfile: ChildProfile){
        currentChildProfileState.value = childProfile
    }

}