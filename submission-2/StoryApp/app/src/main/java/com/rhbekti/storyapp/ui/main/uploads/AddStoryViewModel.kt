package com.rhbekti.storyapp.ui.main.uploads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhbekti.storyapp.data.StoryRepository
import com.rhbekti.storyapp.data.network.model.StoryAddModel
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    var storyData = StoryAddModel()

    fun validate(): Boolean {
        return storyData.description.isBlank()
    }

    suspend fun addStory(imageFile: File) = storyRepository.addStory(imageFile,storyData)
}