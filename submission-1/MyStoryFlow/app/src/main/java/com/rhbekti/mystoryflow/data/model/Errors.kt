package com.rhbekti.mystoryflow.data.model

import com.google.gson.annotations.SerializedName

data class Errors(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)