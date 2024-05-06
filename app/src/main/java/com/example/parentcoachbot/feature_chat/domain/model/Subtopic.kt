package com.example.parentcoachbot.feature_chat.domain.model

import androidx.annotation.DrawableRes
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Subtopic: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var owner_id: String? = null
    var title: RealmDictionary<String> = realmDictionaryOf()
    var code: String? = null
    var topic: String? = null
    var description: RealmDictionary<String> = realmDictionaryOf()
    var defaultQuestionCode: String? = null
    @DrawableRes var icon:Int? = null
}