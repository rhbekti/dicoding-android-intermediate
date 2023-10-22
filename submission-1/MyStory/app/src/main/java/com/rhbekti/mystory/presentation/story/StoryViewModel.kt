package com.rhbekti.mystory.presentation.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhbekti.mystory.data.model.StoryRepository
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getSession() = storyRepository.getSession()

    fun getAllStories(token: String) = storyRepository.getAllStories(token)
}