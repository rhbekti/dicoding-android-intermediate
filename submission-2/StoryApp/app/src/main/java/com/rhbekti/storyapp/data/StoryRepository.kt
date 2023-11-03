package com.rhbekti.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.rhbekti.storyapp.data.db.StoryDatabase
import com.rhbekti.storyapp.data.network.ApiService
import com.rhbekti.storyapp.data.network.model.StoryAddModel
import com.rhbekti.storyapp.data.network.response.DetailStoryResponse
import com.rhbekti.storyapp.data.network.response.ErrorResponse
import com.rhbekti.storyapp.data.network.response.ListStoryItem
import com.rhbekti.storyapp.data.network.response.StoryAddResponse
import com.rhbekti.storyapp.data.paging.StoryRemoteMediator
import com.rhbekti.storyapp.data.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreferences
) {

    suspend fun getStoriesWithLocation(): Flow<Result<List<ListStoryItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response.listStory))
        } catch (e: HttpException) {
            emit(Result.Error(handleError(e)))
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getDetailStory(id: String): Flow<Result<DetailStoryResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(handleError(e)))
        }
    }

    suspend fun addStory(
        imageFile: File,
        storyData: StoryAddModel
    ): Flow<Result<StoryAddResponse>> = flow {
        emit(Result.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val description = storyData.description.toRequestBody("text/plain".toMediaType())
        val lat = storyData.lat.toFloat()
        val lon = storyData.lon.toFloat()

        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        try {
            val response = apiService.addStory(multipartBody, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error(handleError(e)))
        }
    }

    suspend fun logout() = userPreference.logout()

    fun getSession() = userPreference.getSession()

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
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            userPreference: UserPreferences
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(storyDatabase, apiService, userPreference)
        }.also { instance = it }
    }
}