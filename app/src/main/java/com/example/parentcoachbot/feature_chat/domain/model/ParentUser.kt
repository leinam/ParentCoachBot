package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.annotations.PrimaryKey
import io.realm.kotlin.types.RealmObject
import java.util.UUID

open class ParentUser: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var _partition: String? = null
    var name: String = "User"
    var username: String? = null
    var gender: String = Sex.NotSpecified.name
    var country: String? = null
}

