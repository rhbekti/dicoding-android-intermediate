package com.rhbekti.storyapp.data.network.response

import com.google.gson.annotations.SerializedName

data class StoryAddResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
