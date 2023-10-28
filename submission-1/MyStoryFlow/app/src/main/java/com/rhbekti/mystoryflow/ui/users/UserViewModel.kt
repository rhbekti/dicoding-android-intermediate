package com.rhbekti.mystoryflow.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhbekti.mystoryflow.data.Result
import com.rhbekti.mystoryflow.data.UserRepository
import com.rhbekti.mystoryflow.data.model.Users
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // use flow
    /* suspend fun getUsers(): Flow<Result<List<Users>>>{
        return userRepository.getUsers()
    }
    */

    // use live data
    private val _usersList = MutableLiveData<Result<List<Users>>>()
    val usersList: LiveData<Result<List<Users>>> get() = _usersList

    fun getUsers() {
        viewModelScope.launch {
            try {
                _usersList.value = Result.Loading
                userRepository.getUsers().collect {
                    _usersList.value = it
                }
            } catch (e: Exception) {
                _usersList.value = Result.Error(e.message.toString())
            }
        }
    }
}