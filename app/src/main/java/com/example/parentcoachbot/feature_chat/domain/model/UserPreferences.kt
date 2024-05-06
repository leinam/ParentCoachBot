package com.example.parentcoachbot.feature_chat.domain.model

import com.example.parentcoachbot.feature_chat.domain.util.Language
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class UserPreferences: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var owner_id: String? = null
    var parentUser: String? = null
    var username: String? = null
    var defaultLanguage: String? = Language.English.isoCode
    var monitorUsage: Boolean = true
}