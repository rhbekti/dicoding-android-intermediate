package com.rhbekti.mystoryflow.data.di

import com.rhbekti.mystoryflow.data.UserRepository
import com.rhbekti.mystoryflow.data.api.ApiConfig

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}