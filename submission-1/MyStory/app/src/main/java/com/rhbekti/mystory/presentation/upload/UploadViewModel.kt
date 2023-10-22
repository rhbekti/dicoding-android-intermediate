package com.rhbekti.mystory.presentation.upload

import androidx.lifecycle.ViewModel
import com.rhbekti.mystory.data.model.StoryRepository
import java.io.File

class UploadViewModel(private val storyRepository: StoryRepository): ViewModel() {
    var description: String? = null

    fun setDescriptionValue(description: String) {
        this.description = description
    }

    fun validate(): Boolean {
        return description != null
    }

    fun getSession() = storyRepository.getSession()

    fun addStory(token: String,file: File,description: String) = storyRepository.addStory(token,file,description)
}