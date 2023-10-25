package com.example.parentcoachbot.feature_chat.domain.util

import android.app.Application
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary

class PopulateDb(
    private val realm: MutableRealm,
    private val application: Application
) {

    operator fun invoke() {
        println("Attempting to pre-populate database")

        val defaultParentUser = ParentUser()
        val titleDict: RealmDictionary<String?> = realmDictionaryOf(
            Pair(Language.English.isoCode, "Breastfeeding"),
            Pair(Language.Portuguese.isoCode, "Amamentação"),
            Pair(Language.Zulu.isoCode, "Ukuncelisa")
        )

        val breastfeedingTopic: Topic = Topic().apply {
            this.titleEn = "Breastfeeding"
            this.title = titleDict
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