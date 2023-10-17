package com.example.parentcoachbot.feature_chat.domain.model

import androidx.annotation.DrawableRes
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Subtopic: RealmObject {
    @PrimaryKey
    var _id:ObjectId = ObjectId.invoke()
    var titleEn: String? = null
    var titlePt: String? = null
    var titleZu: String? = null
    var code: String? = null
    var topic:ObjectId? = null
    @DrawableRes var icon:Int? = null
    var description: String? = null
}