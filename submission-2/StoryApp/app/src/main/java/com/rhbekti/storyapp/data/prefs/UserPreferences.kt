package com.rhbekti.storyapp.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USER_ID_KEY] ?: "",
                preferences[USER_NAME_KEY] ?: "",
                preferences[USER_TOKEN] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId
            preferences[USER_NAME_KEY] = user.name
            preferences[USER_TOKEN] = user.token
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val USER_NAME_KEY = stringPreferencesKey("name")
        private val USER_TOKEN = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}