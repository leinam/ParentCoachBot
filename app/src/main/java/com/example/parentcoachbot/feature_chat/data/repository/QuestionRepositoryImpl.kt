package com.example.parentcoachbot.feature_chat.data.repository

import android.util.Log
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class QuestionRepositoryImpl(val realm: Realm) : QuestionRepository {
    override suspend fun getAllQuestions(): Flow<List<Question>>? = withContext(Dispatchers.IO) {
        try {
            realm.query<Question>().asFlow().map {
                it.list
            }
        } catch (e: Exception) {
            Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
            null
        }
    }

    override suspend fun getQuestionById(id: String): Question? = withContext(Dispatchers.IO) {
        try {
            realm.query<Question>(query = "_id == $0", id).find().firstOrNull()?.copyFromRealm()
        }
        catch (e: Exception){
            Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
            null
        }
    }

    override suspend fun getQuestionsFromIdList(idList: List<String>): List<Question>? =
        withContext(Dispatchers.IO) {
            try{
                realm.query<Question>().find().copyFromRealm().filter {
                    it._id in idList
                }
            } catch (e: Exception) {
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }

        }

    override suspend fun insertQuestion(question: Question) {
        realm.write {
            this.copyToRealm(question)
        }
    }

    override suspend fun deleteQuestion(id: String) {
        realm.write {
            val question = this.query<Question>("_id == $id").find().firstOrNull()

            question?.let { questionToDelete ->
                findLatest(questionToDelete)?.also { delete(it) }
            }

        }
    }

    override suspend fun getQuestionsBySubtopic(subtopicCode: String): Flow<List<Question>>? =
        withContext(Dispatchers.IO) {
            try{
                realm.query<Question>(query = "subtopic == $0", subtopicCode).find().asFlow().map {
                    it.list
                }
            }
            catch (e: Exception){
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }
        }

    override suspend fun getQuestionsBySubtopicList(subtopicId: String): Flow<List<Question>>? =
        withContext(Dispatchers.IO) {
            try {
                realm.query<Question>().asFlow().map {
                    it.list.copyFromRealm().filter { question ->
                        question.subtopics.contains(subtopicId)
                    }
                }
            }

            catch (e: Exception){
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }
        }
}