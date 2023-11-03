package com.rhbekti.storyapp.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rhbekti.storyapp.data.AuthRepository
import com.rhbekti.storyapp.di.Injection
import com.rhbekti.storyapp.ui.auth.login.LoginViewModel
import com.rhbekti.storyapp.ui.auth.register.RegisterViewModel
import com.rhbekti.storyapp.ui.splash.SplashViewModel

class AuthModelFactory private constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthModelFactory? = null

        fun getInstance(context: Context): AuthModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }
}