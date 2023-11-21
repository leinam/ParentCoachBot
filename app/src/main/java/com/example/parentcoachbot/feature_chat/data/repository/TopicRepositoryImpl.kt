package com.example.parentcoachbot.feature_chat.data.repository

import android.util.Log
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.repository.TopicRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TopicRepositoryImpl(private val realm: Realm) : TopicRepository {
    override suspend fun getAllTopics(): Flow<List<Topic>>? = withContext(Dispatchers.IO) {
        try { realm.query<Topic>().find().asFlow().map { it.list.copyFromRealm() } }
        catch (e: Exception){
            Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
            null
        }
    }

    override suspend fun addTopic(topic: Topic) {
        realm.write {
            this.copyToRealm(instance = topic)
        }
    }

}