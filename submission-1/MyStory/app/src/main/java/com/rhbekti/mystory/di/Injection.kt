package com.rhbekti.mystory.di

import android.content.Context
import com.rhbekti.mystory.data.local.room.StoryDao
import com.rhbekti.mystory.data.local.room.StoryRoomDatabase
import com.rhbekti.mystory.data.pref.UserPreferences
import com.rhbekti.mystory.data.pref.dataStore
import com.rhbekti.mystory.data.model.AuthRepository
import com.rhbekti.mystory.data.model.StoryRepository
import com.rhbekti.mystory.data.remote.retrofit.ApiConfig
import com.rhbekti.mystory.data.remote.retrofit.ApiService
import com.rhbekti.mystory.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        return AuthRepository.getInstance(apiService, userPreferences)
    }

    fun storyProvideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryRoomDatabase.getInstance(context)
        val dao = database.storyDao()
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, dao, userPreferences, appExecutors)
    }
}