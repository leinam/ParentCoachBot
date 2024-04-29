package com.example.parentcoachbot.feature_chat.data.repository

import android.util.Log
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.UserPreferences
import com.example.parentcoachbot.feature_chat.domain.repository.UserPreferencesRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserPreferencesRepositoryImpl(private val realm: Realm): UserPreferencesRepository{
    override suspend fun setDefaultLanguage(languageCode: String, parentUserId: String): Unit =
    withContext(Dispatchers.IO) {
        val userPreference: UserPreferences? =
            realm.query<UserPreferences>("_id == $0", parentUserId).first().find()
        realm.write {
            userPreference?.let {
                findLatest(it)?.let { liveUserPreference ->
                    liveUserPreference.defaultLanguage = languageCode
                }

            }
        }
    }

    override suspend fun newUserPreference(parentUser: ParentUser) {
        realm.write {
            this.copyToRealm(parentUser)
        }
    }

    override suspend fun getUserPreferencesByParentUser(parentUserId: String): Flow<UserPreferences?>? {
        return try {
            realm.query<UserPreferences>(query ="parentUser == $0",parentUserId).find().asFlow().map {
                it.list.firstOrNull()?.copyFromRealm()
            }
        } catch (e: Exception) {
            Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
            null
        }
    }

}