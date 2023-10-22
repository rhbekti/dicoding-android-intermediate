package com.rhbekti.mystory.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.rhbekti.mystory.data.local.entity.StoryEntity
import com.rhbekti.mystory.data.local.room.StoryDao
import com.rhbekti.mystory.data.pref.UserModel
import com.rhbekti.mystory.data.pref.UserPreferences
import com.rhbekti.mystory.data.remote.response.DetailStoryResponse
import com.rhbekti.mystory.data.remote.response.ErrorResponse
import com.rhbekti.mystory.data.remote.response.FileUploadResponse
import com.rhbekti.mystory.data.remote.response.Story
import com.rhbekti.mystory.data.remote.response.StoryResponse
import com.rhbekti.mystory.data.remote.retrofit.ApiService
import com.rhbekti.mystory.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: StoryDao,
    private val userPreferences: UserPreferences,
    private val appExecutors: AppExecutors
) {

    private val result = MediatorLiveData<Result<List<StoryEntity>>>()
    private val detail = MediatorLiveData<Result<Story?>>()
    private val resultUpload = MediatorLiveData<Result<FileUploadResponse?>>()

    fun getAllStories(token: String): LiveData<Result<List<StoryEntity>>> {
        result.value = Result.Loading
        val client = apiService.getAllStories(token)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory
                    val listStory = ArrayList<StoryEntity>()
                    appExecutors.diskIO.execute {
                        stories?.forEach { story ->
                            val str = StoryEntity(
                                story?.id.toString(),
                                story?.name.toString(),
                                story?.description.toString(),
                                story?.photoUrl.toString(),
                                story?.createdAt.toString(),
                                story?.lat.toString(),
                                story?.lon.toString()
                            )
                            listStory.add(str)
                        }
                        storyDao.deleteAll()
                        storyDao.insert(listStory)
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        result.value = errorMessage.message?.let { Result.Error(it) }
                    } catch (e: Exception) {
                        result.value = Result.Error("Error parsing error response")
                    }
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = storyDao.getAllStories()
        result.addSource(localData) { newData: List<StoryEntity> ->
            result.value = Result.Success(newData)
        }

        return result
    }

    fun getDetailStories(token: String, id: String): LiveData<Result<Story?>> {
        detail.value = Result.Loading
        val client = apiService.getDetailStories(token, id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    detail.value = Result.Success(response.body()?.story)
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        detail.value = errorMessage.message?.let { Result.Error(it) }
                    } catch (e: Exception) {
                        detail.value = Result.Error("Error parsing error response")
                    }
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                detail.value = Result.Error(t.message.toString())
            }
        })

        return detail
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    fun addStory(token: String, imageFile: File, description: String) : LiveData<Result<FileUploadResponse?>>
    {
        resultUpload.value = Result.Loading
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = apiService.uploadImage(token,multipartBody,requestBody)
        client.enqueue(object : Callback<FileUploadResponse>{
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if(response.isSuccessful){
                    resultUpload.value = Result.Success(response.body())
                }else{
                   try {
                       val errorBody = response.errorBody()?.string()
                       val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)
                       resultUpload.value = errorMessage.message?.let { Result.Error(it) }
                   }catch (e: Exception){
                       resultUpload.value = Result.Error("Error parsing error response")
                   }
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                resultUpload.value = Result.Error(t.message.toString())
            }
        })

        return resultUpload
    }

    companion object {
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao,
            userPreferences: UserPreferences,
            appExecutors: AppExecutors
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, storyDao, userPreferences, appExecutors)
        }.also { instance = it }
    }
}