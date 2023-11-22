package com.example.parentcoachbot.feature_chat.domain.model

import com.example.parentcoachbot.feature_chat.domain.util.AnswerType
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID
/**
class Answer: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var _partition: String? = null
    var supportingImagePath: String? = null
    var answerText: RealmDictionary<String> = realmDictionaryOf()
    @Index
    var answerType: String? = AnswerType.Info.code
    var answerThread: String? = null
    var answerThreadPosition: Int? = null
}**/

class Answer : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var _partition: String? = null
    var supportingImagePath: String? = null
    var answerText: RealmDictionary<String> = realmDictionaryOf()
    @Index
    var answerType: String? = AnswerType.Info.code
    var answerThread: String? = null
    var answerThreadPosition: Int? = null

    // Empty constructor for Realm
    constructor()

    // Secondary Constructor with parameters for testing
    constructor(id: Int, text: String) {
        // Initialize your properties here using the provided parameters
        // For example:
        _id = id.toString()
        answerText["defaultLanguage"] = text
    }
}