package com.rhbekti.storyapp.di

import android.content.Context
import com.rhbekti.storyapp.data.AuthRepository
import com.rhbekti.storyapp.data.StoryRepository
import com.rhbekti.storyapp.data.db.StoryDatabase
import com.rhbekti.storyapp.data.network.ApiConfig
import com.rhbekti.storyapp.data.prefs.UserPreferences
import com.rhbekti.storyapp.data.prefs.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(database,apiService, pref)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return AuthRepository.getInstance(apiService, pref)
    }
}