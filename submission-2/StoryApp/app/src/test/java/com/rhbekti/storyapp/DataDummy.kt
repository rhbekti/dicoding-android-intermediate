package com.rhbekti.storyapp

import com.rhbekti.storyapp.data.network.response.ListStoryItem
import com.rhbekti.storyapp.data.network.response.StoryResponse

object DataDummy {
    fun generateDummyStoryResponse(): StoryResponse {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                createdAt = "2023-11-02T12:12:12Z",
                description = "Description $i",
                photoUrl = "https://images.unsplash.com/photo-1574217013471-c32c6846cef7?auto=format&fit=crop&q=80&w=1374&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
            listStory.add(story)
        }

        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }
}