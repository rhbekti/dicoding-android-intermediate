package com.rhbekti.storyapp.data.prefs

data class UserModel(
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)