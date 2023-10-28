package com.rhbekti.mystoryflow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rhbekti.mystoryflow.data.UserRepository
import com.rhbekti.mystoryflow.data.di.Injection
import com.rhbekti.mystoryflow.ui.users.UserViewModel

class ViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository())
            }.also { instance = it }
    }
}