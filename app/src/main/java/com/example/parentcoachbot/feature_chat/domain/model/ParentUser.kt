package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

open class ParentUser: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var owner_id: String? = null
    var name: String = "User"
    var username: String? = null
    var gender: String = Sex.NotSpecified.name
    var country: String? = null
    var language: String? = null
}

