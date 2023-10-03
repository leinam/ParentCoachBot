package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class AnswerRepositoryImpl(private val realm: Realm) : AnswerRepository {
    override suspend fun getAnswerById(id: ObjectId): Answer? = withContext(Dispatchers.IO) {
        realm.query<Answer>(query = "_id == $0", id).find().firstOrNull()
    }

    // return flow ?? the answers not likely to change in real time
    override suspend fun getQuestionAnswers(idList: List<ObjectId>): List<Answer> =
        withContext(Dispatchers.IO) {
            realm.query<Answer>(query = "_id IN $0", idList).find()
        }

    override suspend fun getAnswersByAnswerThreadCode(answerThreadCode: String): List<Answer> =
        withContext(Dispatchers.IO) {
            realm.query<Answer>(query = "answerThread == $0", answerThreadCode).find().sortedBy { answer ->
                answer.answerThreadPosition
            }
        }

    override suspend fun deleteAnswer(id: ObjectId) {
        realm.write {
            val answer = realm.query<Answer>(query = "_id == $id").find().firstOrNull()
            answer?.let {
                delete(answer)
            }

        }
    }

    override suspend fun insertAnswer(answer: Answer) {
        realm.write {
            this.copyToRealm(answer)
        }
    }

}