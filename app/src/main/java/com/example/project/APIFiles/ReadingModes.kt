package com.example.project.APIFiles


import com.squareup.moshi.Json

data class ReadingModes(
    @Json(name = "image")
    val image: Boolean? = null,
    @Json(name = "text")
    val text: Boolean? = null
)