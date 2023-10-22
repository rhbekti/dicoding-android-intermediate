package com.rhbekti.mystory.presentation.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rhbekti.mystory.data.model.StoryRepository
import com.rhbekti.mystory.di.Injection
import com.rhbekti.mystory.presentation.detail.DetailStoryViewModel
import com.rhbekti.mystory.presentation.story.StoryViewModel
import com.rhbekti.mystory.presentation.upload.UploadViewModel

class StoryModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryModelFactory? = null

        fun getInstance(context: Context): StoryModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryModelFactory(Injection.storyProvideRepository(context))
            }.also { instance = it }
    }
}