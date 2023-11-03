package com.rhbekti.storyapp.ui.main.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhbekti.storyapp.data.StoryRepository
import com.rhbekti.storyapp.data.network.response.ListStoryItem
import com.rhbekti.storyapp.data.Result
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private var _listStoryPlace = MutableLiveData<Result<List<ListStoryItem>>>()
    val listStoryPlace: LiveData<Result<List<ListStoryItem>>> = _listStoryPlace

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                _listStoryPlace.value = Result.Loading
                storyRepository.getStoriesWithLocation().collect {
                    _listStoryPlace.value = it
                }
            } catch (e: Exception) {
                _listStoryPlace.value = Result.Error(e.message.toString())
            }
        }
    }
}