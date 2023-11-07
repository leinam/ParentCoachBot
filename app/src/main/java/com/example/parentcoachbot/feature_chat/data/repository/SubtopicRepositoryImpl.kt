package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SubtopicRepositoryImpl(private val realm: Realm): SubtopicRepository {
    override suspend fun getAllSubtopics(): Flow<List<Subtopic>> = withContext(Dispatchers.IO) {
        realm.query<Subtopic>().find().asFlow().map { it.list }
    }

    override suspend fun getSubtopicsByTopic(topicId: String): Flow<List<Subtopic>> = withContext(Dispatchers.IO){
        realm.query<Subtopic>(query = "topic == $0", topicId).find().asFlow().map { it.list }
    }

    override suspend fun getSubtopicsById(id: String): Subtopic? = withContext(Dispatchers.IO){
        realm.query<Subtopic>(query = "_id == $0", id).find().firstOrNull()
    }

    override suspend fun getSubtopicsByCode(code: String): Subtopic? = withContext(Dispatchers.IO){
        realm.query<Subtopic>(query = "code == $0", code).find().firstOrNull()
    }

    override suspend fun addSubtopic(subtopic: Subtopic) {
        realm.write {
            this.copyToRealm(subtopic)
        }
    }

}