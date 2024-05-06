package com.example.parentcoachbot.feature_chat.presentation.contact_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.common.LoggingEvent
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.AnswerUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.SubtopicUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AppPreferences
import com.example.parentcoachbot.feature_chat.domain.util.AuthManager
import com.example.parentcoachbot.feature_chat.domain.util.QuestionSearcher
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactScreenViewModel @Inject constructor(
    private val application: Application,
    private val appPreferences: AppPreferences,
    val firebaseAnalytics: FirebaseAnalytics,
    private val eventLogger: EventLogger,
    private val questionUseCases: QuestionUseCases,
    private val questionSessionUseCases: QuestionSessionUseCases,
    private val topicUseCases: TopicUseCases,
    private val subtopicUseCases: SubtopicUseCases,
    private val childProfileUseCases: ChildProfileUseCases,
    private val parentUserUseCases: ParentUserUseCases,
    private val answerUseCases: AnswerUseCases,
    private val chatSessionUseCases: ChatSessionUseCases,
    private val globalState: GlobalState,
    private val authManager: AuthManager,
    private val questionSearcher: QuestionSearcher
) : ViewModel() {

    private val _currentChildProfile = globalState.currentChildProfileState
    private val parentUserState = globalState.parentUserState
    private val _currentLanguageCode = globalState.currentLanguageCode
    fun onEvent(contactScreenEvent: ContactScreenEvent) {
        when (contactScreenEvent) {
            is ContactScreenEvent.SelectContact -> {
                val contactItem = contactScreenEvent.contactItem

                parentUserState.value?.let {
                    eventLogger.logContactEvent(
                        language = _currentLanguageCode.value, country = contactItem.country,
                        parentUser = it, loggingEvent = LoggingEvent.SelectContact,
                        contactCategory = contactItem.categoryId,
                        contactTitle = contactItem.id,
                        phoneContact = contactItem.phoneContact ?: "null",
                        profile = _currentChildProfile.value
                    )
                }
            }


            is ContactScreenEvent.ViewContactCategory -> {

                val contactCategory = contactScreenEvent.contactCategory

                parentUserState.value?.let {
                    eventLogger.logContactEvent(
                        language = _currentLanguageCode.value, country = it.country ?: "null",
                        parentUser = it, loggingEvent = LoggingEvent.ViewContactCategory,
                        contactCategory = contactCategory.id, profile = _currentChildProfile.value
                    )
                }

            }
        }

    }
}