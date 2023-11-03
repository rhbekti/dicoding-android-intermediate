package com.rhbekti.storyapp.ui.auth.login

import androidx.lifecycle.ViewModel
import com.rhbekti.storyapp.data.AuthRepository
import com.rhbekti.storyapp.data.network.model.LoginModel
import com.rhbekti.storyapp.data.prefs.UserModel

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val account: LoginModel = LoginModel()

    suspend fun login() = authRepository.login(account)

    suspend fun saveSession(user: UserModel) = authRepository.saveSession(user)

    fun getSession() = authRepository.getSession()

    fun validate(): Boolean {
        return account.email.isBlank() && account.password.isBlank()
    }
}