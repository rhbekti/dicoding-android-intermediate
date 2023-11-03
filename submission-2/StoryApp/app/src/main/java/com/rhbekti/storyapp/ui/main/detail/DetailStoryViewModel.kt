package com.rhbekti.storyapp.ui.main.detail

import androidx.lifecycle.ViewModel
import com.rhbekti.storyapp.data.StoryRepository

class DetailStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    suspend fun getDetailStory(id: String) = storyRepository.getDetailStory(id)
}