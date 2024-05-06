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
    private val application: Application,
    private val globalOwnerId: String = "all-users",
    private val userId: String
) {

    operator fun invoke() {
        println("Attempting to pre-populate database")

        val defaultParentUser = ParentUser().apply { this.owner_id = userId }
        val titleDict: RealmDictionary<String> = realmDictionaryOf(
            Pair(Language.English.isoCode, "Child Care"),
            Pair(Language.Portuguese.isoCode, "Cuidados Infantis"),
            Pair(Language.Zulu.isoCode, "Ukunakekelwa Kwengane")
        )

        val childCareTopic: Topic = Topic().apply {
            this.title = titleDict
            this.icon = R.drawable.breastfeeding
            this.owner_id = globalOwnerId
        }

        // realm.copyToRealm(childCareTopic)
        realm.copyToRealm(defaultParentUser)

        // ContentImporter(application.applicationContext, realm = realm, topicId = childCareTopic._id, owner_id = globalOwnerId).importContent()

        println("Done pre-populating database")

    }


}