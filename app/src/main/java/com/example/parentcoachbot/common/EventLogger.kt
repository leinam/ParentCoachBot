package com.example.parentcoachbot.common

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class EventLogger @Inject constructor(val firebaseAnalytics: FirebaseAnalytics){


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
}