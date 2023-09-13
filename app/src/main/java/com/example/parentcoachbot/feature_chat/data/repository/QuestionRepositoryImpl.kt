package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class QuestionRepositoryImpl(val realm: Realm) : QuestionRepository {
    override suspend fun getAllQuestions(): Flow<List<Question>> = withContext(Dispatchers.IO) {
        realm.query<Question>().asFlow().map { it.list }
    }

    override suspend fun getQuestionById(id: ObjectId): Question? = withContext(Dispatchers.IO) {
        realm.query<Question>(query = "_id == $0", id).find().firstOrNull()
    }

    override suspend fun insertQuestion(question: Question) {
        realm.write {
            this.copyToRealm(question)
        }
    }

    override suspend fun deleteQuestion(id: ObjectId) {
        realm.write {
            val question = this.query<Question>("_id == $id").find().first()
            delete(question)
        }
    }

    override suspend fun getQuestionsBySubtopic(subtopicId: ObjectId): Flow<List<Question>> =
        withContext(Dispatchers.IO) {
            realm.query<Question>().asFlow().map {
                it.list.filter { question ->
                    question.subtopics.contains(subtopicId)
                }
            }
        }
}