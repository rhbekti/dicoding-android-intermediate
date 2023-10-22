package com.rhbekti.mystory.presentation.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rhbekti.mystory.data.model.AuthRepository
import com.rhbekti.mystory.di.Injection
import com.rhbekti.mystory.presentation.auth.AuthViewModel

class AuthModelFactory private constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthModelFactory? = null

        fun getInstance(context: Context): AuthModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}