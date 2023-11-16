package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnswerThreadRepositoryImpl(private val realm: Realm): AnswerThreadRepository{
    override suspend fun getAnswerThreadById(id: String): AnswerThread? = withContext(Dispatchers.IO){
        realm.query<AnswerThread>(query = "_id == $0", id).find().firstOrNull()?.copyFromRealm()
    }

    override suspend fun getAnswerThreadByCode(answerCode: String): AnswerThread? = withContext(Dispatchers.IO){
        realm.query<AnswerThread>(query = "code == $0", answerCode).find().firstOrNull()?.copyFromRealm()
    }

    override suspend fun deleteAnswerThread(id: String): Unit = withContext(Dispatchers.IO) {
        realm.write {
            val answerThread = realm.query<AnswerThread>(query = "_id == $0", id).find().firstOrNull()?.copyFromRealm()

            answerThread?.let { thread ->
                findLatest(thread)?.also { delete(it) }
            }
        }
    }

    override suspend fun insertAnswerThread(answerThread: AnswerThread)  {
        realm.write {
            copyToRealm(answerThread)
        }
    }

}