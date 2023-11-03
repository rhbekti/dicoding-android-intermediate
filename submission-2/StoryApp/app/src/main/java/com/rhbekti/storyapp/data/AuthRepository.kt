package com.rhbekti.storyapp.data

import com.google.gson.Gson
import com.rhbekti.storyapp.data.network.ApiService
import com.rhbekti.storyapp.data.network.model.LoginModel
import com.rhbekti.storyapp.data.network.model.RegisterModel
import com.rhbekti.storyapp.data.network.response.ErrorResponse
import com.rhbekti.storyapp.data.network.response.LoginResponse
import com.rhbekti.storyapp.data.network.response.RegisterResponse
import com.rhbekti.storyapp.data.prefs.UserModel
import com.rhbekti.storyapp.data.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    suspend fun register(account: RegisterModel): Flow<Result<RegisterResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.register(
                account.name,
                account.email,
                account.password
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(handleError(e)))
        }
    }

    suspend fun login(account: LoginModel): Flow<Result<LoginResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.login(
                account.email,
                account.password
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(handleError(e)))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    private fun handleError(e: HttpException): String {
        return try {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            errorBody.message.toString()
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    companion object {
        private var instance: AuthRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): AuthRepository = instance ?: synchronized(this) {
            instance ?: AuthRepository(apiService, userPreferences)
        }.also { instance = it }
    }
}