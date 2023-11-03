package com.rhbekti.storyapp.ui.auth.register

import androidx.lifecycle.ViewModel
import com.rhbekti.storyapp.data.AuthRepository
import com.rhbekti.storyapp.data.network.model.RegisterModel

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val account: RegisterModel = RegisterModel()

    suspend fun register() = authRepository.register(account)

    fun validate(): Boolean {
        return account.name.isBlank() && account.email.isBlank() && account.password.isBlank()
    }
}