package com.rhbekti.storyapp.ui.main.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rhbekti.storyapp.data.StoryRepository
import com.rhbekti.storyapp.data.network.response.ListStoryItem
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val listStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getSession() = storyRepository.getSession()
}