package com.example.parentcoachbot.feature_chat.domain.model

import androidx.annotation.DrawableRes
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Topic: RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var titleEn: String? = null
    var title: RealmDictionary<String?> = realmDictionaryOf()
    @DrawableRes var icon: Int? = null
    var description: String? = null
    var topicAlias: String? = null
}


