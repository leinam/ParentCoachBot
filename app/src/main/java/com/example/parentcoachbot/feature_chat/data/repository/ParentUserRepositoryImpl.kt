package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

// TODO Inject Dispatchers
class ParentUserRepositoryImpl(private val realm: Realm) : ParentUserRepository {
    override suspend fun newParentUser(parent: ParentUser) {
        realm.write {
            this.copyToRealm(parent)
        }
    }

    override suspend fun deleteParentUser(id: ObjectId) {
        realm.write {
            val parent = realm.query<ParentUser>(query = "_id == $0", id).find().firstOrNull()
            parent?.let {
                delete(parent)
            }

        }
    }

    override suspend fun getParentUserById(id: ObjectId): ParentUser? =
        withContext(Dispatchers.IO) {
            realm.query<ParentUser>(query = "id == $0", id).find().firstOrNull()
        }

    override fun getParentUser(): ParentUser? {
        return realm.query<ParentUser>().find().firstOrNull()
    }
}