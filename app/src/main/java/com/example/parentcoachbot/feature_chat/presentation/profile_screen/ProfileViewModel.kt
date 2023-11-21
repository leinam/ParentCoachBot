package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val childProfileUseCases: ChildProfileUseCases,
    private val parentUserUseCases: ParentUserUseCases,
    private val globalState: GlobalState,
    private val authManager: AuthManager,
) : ViewModel() {

    var getChildProfilesJob: Job? = null
    private val parentUserState = globalState.parentUserState
    private val _childProfilesList = globalState.childProfilesListState
    private val _currentChildProfile = globalState.currentChildProfileState
    private val _currentLanguageCode = globalState.currentLanguageCode


    private val _profileViewModelState = mutableStateOf(
        ProfileStateWrapper(
            parentUserState = parentUserState,
            childProfilesListState = _childProfilesList,
            currentChildProfileState = _currentChildProfile,
            currentLanguageCode = _currentLanguageCode
        )
    )

    val profileViewModelState: State<ProfileStateWrapper> = _profileViewModelState

    init {
        viewModelScope.launch {
            globalState.getParentUser()
        }
        getChildProfilesList()
    }

    private fun getChildProfilesList() {
        getChildProfilesJob?.cancel()

        getChildProfilesJob = viewModelScope.launch {
            parentUserState.onEach { parentUser ->
                parentUser?.let {
                    childProfileUseCases.getChildProfilesByParentUser(parentUser._id)?.onEach {
                        _childProfilesList.value = it
                    }?.collect()
                }
            }.collect()
        }
    }

    private fun getFullChildProfilesList() {
        getChildProfilesJob?.cancel()

        getChildProfilesJob = viewModelScope.launch {

            childProfileUseCases.getAllChildProfiles()?.onEach {
                _childProfilesList.value = it
            }?.collect()

        }
    }

    fun onEvent(profileEvent: ProfileEvent) {
        when (profileEvent) {
            is ProfileEvent.selectProfile -> {
                _currentChildProfile.value = profileEvent.childProfile
            }

            is ProfileEvent.newProfile -> {
                viewModelScope.launch {
                    childProfileUseCases.newChildProfile(profileEvent.childProfile.apply {
                        this._partition = authManager.authenticatedRealmUser.value?.id
                    })
                }

            }
        }
    }


}