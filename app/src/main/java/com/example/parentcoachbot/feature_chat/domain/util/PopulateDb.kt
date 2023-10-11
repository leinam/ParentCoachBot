package com.example.parentcoachbot.feature_chat.domain.util

import android.app.Application
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import io.realm.kotlin.MutableRealm

class PopulateDb(
    private val realm: MutableRealm,
    private val application: Application
) {


    operator fun invoke() {
        println("Attempting to pre-populate database")

        val defaultParentUser = ParentUser()

        val breastfeedingTopic: Topic = Topic().apply {
            this.title = "Breastfeeding"
            this.icon = R.drawable.breastfeeding_icon
        }

        realm.copyToRealm(breastfeedingTopic)
        realm.copyToRealm(defaultParentUser)

        ContentImporter(
            application.applicationContext,
            realm = realm,
            topicId = breastfeedingTopic._id
        ).importContent()


        println("Done pre-populating database")

    }


}