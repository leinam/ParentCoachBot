package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnswerRepositoryImpl(private val realm: Realm) : AnswerRepository {
    override suspend fun getAnswerById(id: String): Answer? = withContext(Dispatchers.IO) {
        realm.query<Answer>(query = "_id == $0", id).find().firstOrNull()?.copyFromRealm()
    }

    // return flow ?? the answers not likely to change in real time
    override suspend fun getQuestionAnswers(idList: List<String>): List<Answer> =
        withContext(Dispatchers.IO) {
            realm.query<Answer>(query = "_id IN $0", idList).find().copyFromRealm()
        }

    override suspend fun getAnswersByAnswerThreadCode(answerThreadCode: String): List<Answer> =
        withContext(Dispatchers.IO) {
            realm.query<Answer>(query = "answerThread == $0", answerThreadCode).find()
                .copyFromRealm()
                .sortedBy { answer ->
                    answer.answerThreadPosition
                }
        }

    override suspend fun deleteAnswer(id: String) {
        realm.write {
            val answer = realm.query<Answer>(query = "_id == $id").find().firstOrNull()
            answer?.let { answerToDelete: Answer ->
                findLatest(answerToDelete)?.also { delete(it) }
            }
        }
    }

    override suspend fun insertAnswer(answer: Answer) {
        realm.write {
            this.copyToRealm(answer)
        }

    }

}