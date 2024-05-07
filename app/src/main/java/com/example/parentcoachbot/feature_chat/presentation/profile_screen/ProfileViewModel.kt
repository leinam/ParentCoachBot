package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.common.LoggingEvent
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AppPreferences
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val firebaseAnalytics: FirebaseAnalytics,
    private val appPreferences: AppPreferences,
    private val eventLogger: EventLogger
) : ViewModel() {

    private var getChildProfilesJob: Job? = null
    private val _parentUserState = globalState.parentUserState
    private val _childProfilesList = globalState.childProfilesListState
    private val _currentChildProfile = globalState.currentChildProfileState
    private val _currentLanguageCode = globalState.currentLanguageCode
    private val _currentCountry = globalState.currentCountry
    private val _appPreferences: StateFlow<AppPreferences> = MutableStateFlow(appPreferences)
    var getParentUserJob: Job? = null



    private val _profileViewModelState = mutableStateOf(
        ProfileStateWrapper(
            parentUserState = _parentUserState,
            childProfilesListState = _childProfilesList,
            currentChildProfileState = _currentChildProfile,
            currentLanguageCode = _currentLanguageCode,
            appPreferences = _appPreferences,
            currentCountry = _currentCountry
        )
    )

    val profileViewModelState: State<ProfileStateWrapper> = _profileViewModelState

    init {
        getParentUserJob?.cancel()

        getParentUserJob = viewModelScope.launch {
            globalState.getParentUser()


        }

        getChildProfilesList()
    }

    private fun getChildProfilesList() {
        getChildProfilesJob?.cancel()

        getChildProfilesJob = viewModelScope.launch {
            _parentUserState.onEach { parentUser ->
                parentUser?.let {
                    childProfileUseCases.getChildProfilesByParentUser(parentUser._id)?.onEach {
                        _childProfilesList.value = it
                    }?.collect()
                }
            }.collect()
        }
    }

    private suspend fun updateParentAccount(
        parentUser: ParentUser,
        username: String,
        country: String
    ) {
        parentUserUseCases.updateUsername(parentUser._id, username)
        parentUserUseCases.updateCountry(parentUser._id, country)

        appPreferences.setIsAccountSetUp(true)
    }

    private suspend fun updateParentUserAccountCountry(
        parentUser: ParentUser,
        country: String
    ) {
        parentUserUseCases.updateCountry(parentUser._id, country)
    }


    fun onEvent(profileEvent: ProfileEvent) {
        when (profileEvent) {
            is ProfileEvent.SelectProfile -> {
                globalState.updateCurrentChildProfile(profileEvent.childProfile)

                _parentUserState.value?.let { parentUser ->
                    eventLogger.logProfileEvent(
                        loggingEvent = LoggingEvent.SelectProfile,
                        parentUser = parentUser,
                        profile = profileEvent.childProfile
                    )
                }
            }

            is ProfileEvent.NewProfile -> {
                viewModelScope.launch {
                    childProfileUseCases.newChildProfile(profileEvent.childProfile.apply {
                        this.owner_id = authManager.authenticatedRealmUser.value?.id
                        this.parentUsername = _parentUserState.value?.username
                        this.parentUser = _parentUserState.value?._id
                    })
                }

                _parentUserState.value?.let { parentUser ->
                    eventLogger.logProfileEvent(
                        loggingEvent = LoggingEvent.NewProfile,
                        parentUser = parentUser,
                        profile = profileEvent.childProfile
                    )
                }

            }

            is ProfileEvent.DeleteProfile -> {
                viewModelScope.launch {
                    childProfileUseCases.deleteChildProfile(profileEvent.childProfile)
                }

                _parentUserState.value?.let { parentUser ->
                    eventLogger.logProfileEvent(
                        loggingEvent = LoggingEvent.DeleteProfile,
                        parentUser = parentUser,
                        profile = profileEvent.childProfile
                    )
                }

            }

            is ProfileEvent.DeleteAllProfileData -> {
                viewModelScope.launch {
                    childProfileUseCases.deleteAllProfileData(profileEvent.childProfile)
                }

            }

            is ProfileEvent.UpdateUserAccount -> {
                viewModelScope.launch {
                    updateParentAccount(
                        parentUser = profileEvent.parentUser,
                        username = profileEvent.username,
                        country = profileEvent.country
                    )
                }
            }

            is ProfileEvent.UpdateProfileName -> {
                viewModelScope.launch {
                    val updatedProfile = childProfileUseCases.updateProfileName(
                        profileEvent.childProfile,
                        profileEvent.profileName
                    )

                    _currentChildProfile.value = globalState.currentChildProfileState.value
                    _profileViewModelState.value.currentChildProfileState = _currentChildProfile

                }
            }

            is ProfileEvent.UpdateCountry -> {
                viewModelScope.launch {

                    updateParentUserAccountCountry(
                        parentUser = profileEvent.parentUser,
                        country = profileEvent.country
                    )

                    _parentUserState.value?.let { parentUser ->
                        eventLogger.logChangeCountryEvent(
                            country = profileEvent.country,
                            parentUser = parentUser,
                            loggingEvent = LoggingEvent.ChangeCountry,
                            profile = _currentChildProfile.value
                        )
                    }


                }
            }
        }
    }


}