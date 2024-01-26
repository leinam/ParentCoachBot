package com.example.parentcoachbot.common

import android.os.Bundle
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class EventLogger @Inject constructor(val firebaseAnalytics: FirebaseAnalytics) {


    fun logComposableLoad(composableName: String) {
        val bundle = Bundle()
        bundle.putString("screen_name", composableName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logComposableUnload(composableName: String) {
        val bundle = Bundle()
        bundle.putString("screen_name", composableName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logQuestionEvent(
        question: Question,
        parentUser: ParentUser,
        profile: ChildProfile,
        loggingEvent: LoggingEvent
    ) {
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser._partition)
        bundle.putString("profile_id", profile._id)
        bundle.putString("profile_name", profile.name)
        bundle.putString("question_id", question._id)
        bundle.putString("question_text", question.questionText["en"])

        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }


}

sealed class LoggingEvent(val eventName: String) {
    object SaveQuestion : LoggingEvent("save_question")
    object AddQuestion : LoggingEvent("add_question")
    object DeleteQuestion : LoggingEvent("delete_question")
    object PinChat : LoggingEvent("pin_chat")
    object NewChat : LoggingEvent("new_chat")
    object DeleteChat : LoggingEvent("delete_chat")
    object CreateProfile : LoggingEvent("create_profile")
    object NewSearchQuery: LoggingEvent("search_query")
    object SubtopicView: LoggingEvent("subtopic_view")
}