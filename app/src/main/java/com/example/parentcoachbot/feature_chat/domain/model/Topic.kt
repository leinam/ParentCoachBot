package com.example.parentcoachbot.feature_chat.domain.model

import androidx.annotation.DrawableRes
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Topic: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var _partition: String? = null
    var title: RealmDictionary<String> = realmDictionaryOf()
    @DrawableRes var icon: Int? = null
    var description: String? = null
    var topicAlias: String? = null
}


