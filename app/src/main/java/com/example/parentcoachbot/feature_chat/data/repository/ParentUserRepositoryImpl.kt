package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// TODO Inject Dispatchers
class ParentUserRepositoryImpl(private val realm: Realm) : ParentUserRepository {
    override suspend fun newParentUser(parent: ParentUser) {
        realm.write {
            this.copyToRealm(parent)
        }
    }

    override suspend fun deleteParentUser(id: String) {
        realm.write {
            val parent = realm.query<ParentUser>(query = "_id == $0", id).find().firstOrNull()
            parent?.let {user ->
                findLatest(user)?.also { delete(it) }
            }

        }
    }

    override suspend fun getParentUserById(id: String): ParentUser? =
        withContext(Dispatchers.IO) {
            realm.query<ParentUser>(query = "id == $0", id).find().firstOrNull()?.copyFromRealm()
        }

    override fun getParentUser(): Flow<ParentUser?> {
        return realm.query<ParentUser>().find().asFlow().map {
            it.list.firstOrNull()?.copyFromRealm()
        }
    }
}