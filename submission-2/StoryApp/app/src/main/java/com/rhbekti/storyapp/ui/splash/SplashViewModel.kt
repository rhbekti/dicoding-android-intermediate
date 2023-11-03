package com.rhbekti.storyapp.ui.splash

import androidx.lifecycle.ViewModel
import com.rhbekti.storyapp.data.AuthRepository

class SplashViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getSession() = authRepository.getSession()
}