package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class ChildProfileRepositoryImpl(private val realm: Realm): ChildProfileRepository {
    override suspend fun newChildProfile(childProfile: ChildProfile) {
        realm.write {
            this.copyToRealm(childProfile)
        }
    }

    override suspend fun getChildProfileByParent(parentId: ObjectId): Flow<List<ChildProfile>> = withContext(
        Dispatchers.IO){
        realm.query<ChildProfile>(query = "parentUser == $0", parentId).find().asFlow()
            .map { it.list }
    }

    override suspend fun getChildProfileByParentTest(parentId: ObjectId): ChildProfile = withContext(
        Dispatchers.IO){
        realm.query<ChildProfile>(query = "parentUser == $0", parentId).find().first()
    }

    override suspend fun getChildProfileById(id: ObjectId): ChildProfile = withContext(Dispatchers.IO){
        realm.query<ChildProfile>(query = "_id == $0", id).find().first()
    }

    override suspend fun deleteChildProfile(id: ObjectId) {
        realm.write {
            val childProfile = realm.query<ChildProfile>(query = "_id == $0", id).find().first()
            delete(childProfile)
        }
    }

}