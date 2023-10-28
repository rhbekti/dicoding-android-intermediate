package com.rhbekti.mystoryflow.data.api

import com.rhbekti.mystoryflow.data.model.Users
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers() : List<Users>
}