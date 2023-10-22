package com.rhbekti.mystory.presentation.detail

import androidx.lifecycle.ViewModel
import com.rhbekti.mystory.data.model.StoryRepository

class DetailStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getDetailStory(token: String, id: String) = storyRepository.getDetailStories(token, id)

    fun getSession() = storyRepository.getSession()
}