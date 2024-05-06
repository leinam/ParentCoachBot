package com.example.parentcoachbot.common

import android.os.Bundle
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.presentation.resources_screens.ResourceItem
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
        question: Question?,
        questionSession: QuestionSession?,
        parentUser: ParentUser,
        profile: ChildProfile,
        loggingEvent: LoggingEvent,
        previousQuestion: Question? = null
    ) {

        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile._id)
        bundle.putString("previous_question_code", previousQuestion?.questionCode ?: "null")
        bundle.putString("previous_question_text", previousQuestion?.questionText?.get("en")  ?: "null")
        bundle.putString("profile_name", profile.name)
        bundle.putString("question_id", question?._id ?: "null")
        bundle.putString("question_code", question?.questionCode ?: "null")
        bundle.putString("question_text", question?.questionText?.get("en")  ?: "null")
        bundle.putString("question_session_id", questionSession?._id ?: "null")
        bundle.putString("subtopic", question?.subtopic ?: "null")

        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logChatEvent(
        chatSession: ChatSession,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("chat_session_id", chatSession._id)


        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logProfileEvent(
        parentUser: ParentUser,
        profile: ChildProfile,
        loggingEvent: LoggingEvent
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile._id)
        bundle.putString("profile_name", profile.name)


        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logSearchEvent(
        searchQueryText: String,
        chatSession: ChatSession?,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent,
        currentLanguage: String
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("chatSession_id", chatSession?._id ?: "null")
        bundle.putString("search_query_text", searchQueryText)
        bundle.putString("current_language", currentLanguage)



        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logSubtopicEvent(
        chatSession: ChatSession?,
        subtopic: Subtopic,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("subtopic_id", subtopic._id)
        bundle.putString("subtopic_code", subtopic.code)
        bundle.putString("subtopic_title", subtopic.title["en"])
        bundle.putString("chat_session_id", chatSession?._id ?: "null")


        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logChangeCountryEvent(
        country: String,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("new_country", country)


        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logChangeLanguageEvent(
        language: String,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("new_language", language)


        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logContactEvent(
        language: String,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent,
        country: String,
        phoneContact: String = "null",
        contactCategory: String,
        contactTitle: String = "null"
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("language", language)
        bundle.putString("country", country)
        bundle.putString("contact_category", contactCategory)
        bundle.putString("contact_title", contactTitle)
        bundle.putString("phone_contact", phoneContact)

        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }

    fun logResourceEvent(
        language: String,
        parentUser: ParentUser,
        profile: ChildProfile?,
        loggingEvent: LoggingEvent,
        resourceItem: ResourceItem
    ){
        val bundle = Bundle()
        bundle.putString("username", parentUser.username)
        bundle.putString("parent_user_id", parentUser._id)
        bundle.putString("auth_id", parentUser.owner_id)
        bundle.putString("profile_id", profile?._id ?: "null")
        bundle.putString("profile_name", profile?.name ?: "null")
        bundle.putString("language", language)
        bundle.putString("country", resourceItem.country)
        bundle.putString("resource_title", resourceItem.title["en"])
        bundle.putString("resource_type", resourceItem.contentType)
        bundle.putString("resource_source", resourceItem.source)

        firebaseAnalytics.logEvent(loggingEvent.eventName, bundle)
    }


}

sealed class LoggingEvent(val eventName: String) {
    object SaveQuestion : LoggingEvent("save_question")
    object UnsaveQuestion : LoggingEvent("unsave_question")
    object AddQuestion : LoggingEvent("add_question")
    object AddRelatedQuestion : LoggingEvent("add_related_question")
    object DeleteQuestion : LoggingEvent("delete_question")
    object PinChat : LoggingEvent("pin_chat")
    object NewChat : LoggingEvent("new_chat")
    object LoadChat: LoggingEvent("load_chat")
    object DeleteChat : LoggingEvent("delete_chat")
    object NewProfile : LoggingEvent("create_profile")
    object DeleteProfile : LoggingEvent("delete_profile")
    object SelectProfile : LoggingEvent("select_profile")
    object NewSearchQuery: LoggingEvent("search_query")
    object SubtopicView: LoggingEvent("subtopic_view")
    object ChangeLanguage: LoggingEvent("change_language")
    object ChangeCountry: LoggingEvent("change_country")
    object SelectContact: LoggingEvent("select_contact")
    object ViewContactCategory: LoggingEvent("view_contact_category")
    object ViewResourceItem: LoggingEvent("view_resource")
}