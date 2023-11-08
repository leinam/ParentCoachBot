package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.RealmSyncRepository
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
    private val syncRepository: RealmSyncRepository
) : ViewModel() {


    var getChildProfilesJob: Job? = null
    private val parentUser = globalState.parentUserState
    private val _childProfilesList = globalState._childProfilesListState
    private val _currentChildProfile = globalState._currentChildProfileState

    private val _profileViewModelState = mutableStateOf(
        ProfileStateWrapper(
            parentUserState = parentUser,
            childProfilesListState = _childProfilesList,
            currentChildProfileState = _currentChildProfile
        )
    )

    val profileViewModelState: State<ProfileStateWrapper> = _profileViewModelState

    init {
        getChildProfilesList()

        viewModelScope.launch {
            syncRepository.populateDatabase()
        }

    }

    private fun getChildProfilesList() {
        getChildProfilesJob?.cancel()

        getChildProfilesJob = viewModelScope.launch {
            parentUser.onEach { parentUser ->
                parentUser?.let {
                    childProfileUseCases.getChildProfilesByParentUser(parentUser._id).onEach {
                        _childProfilesList.value = it
                    }.collect()
                }
            }.collect()
        }
    }

    private fun getFullChildProfilesList() {
        getChildProfilesJob?.cancel()

        getChildProfilesJob = viewModelScope.launch {

            childProfileUseCases.getAllChildProfiles().onEach {
                _childProfilesList.value = it
            }.collect()

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
                        this._partition = authManager.realmUser.value?.id
                    })
                }

            }
        }
    }


}