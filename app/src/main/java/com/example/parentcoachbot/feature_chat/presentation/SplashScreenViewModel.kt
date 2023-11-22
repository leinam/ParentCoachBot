package com.example.parentcoachbot.feature_chat.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.AuthResult
import com.example.parentcoachbot.feature_chat.domain.util.RealmSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val syncRepository: RealmSyncRepository,
    private val authManager: AuthManager,
    private val application: Application
) : ViewModel() {

    private val _authenticationResultState: MutableStateFlow<AuthResult?> = MutableStateFlow(null)
    val splashScreenViewModelState: State<SplashScreenStateWrapper> = mutableStateOf(
        SplashScreenStateWrapper(
            authenticationResultState = _authenticationResultState,
            application = MutableStateFlow(application)
        )
    )

    // TODO failing to set the parttion id immediately so first write fails

    init {
        populateDatabase()
    }

    private fun populateDatabase() {
        viewModelScope.launch {
            syncRepository.populateDatabase()
        }
    }

}