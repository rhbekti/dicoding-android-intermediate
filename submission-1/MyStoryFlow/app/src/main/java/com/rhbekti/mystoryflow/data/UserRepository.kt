package com.rhbekti.mystoryflow.data

import com.google.gson.Gson
import com.rhbekti.mystoryflow.data.api.ApiService
import com.rhbekti.mystoryflow.data.model.Errors
import com.rhbekti.mystoryflow.data.model.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getUsers(): Flow<Result<List<Users>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getUsers()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Errors::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService)
        }.also { instance = it }
    }
}