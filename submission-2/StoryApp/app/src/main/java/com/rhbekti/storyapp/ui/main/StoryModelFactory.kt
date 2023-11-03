package com.rhbekti.storyapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rhbekti.storyapp.data.StoryRepository
import com.rhbekti.storyapp.di.Injection
import com.rhbekti.storyapp.ui.main.detail.DetailStoryViewModel
import com.rhbekti.storyapp.ui.main.maps.MapsViewModel
import com.rhbekti.storyapp.ui.main.story.StoryViewModel
import com.rhbekti.storyapp.ui.main.uploads.AddStoryViewModel

class StoryModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryModelFactory? = null
        fun getInstance(context: Context): StoryModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}