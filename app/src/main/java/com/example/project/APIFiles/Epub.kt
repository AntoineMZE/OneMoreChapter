package com.example.project.APIFiles


import com.squareup.moshi.Json

data class Epub(
    @Json(name = "acsTokenLink")
    val acsTokenLink: String? = null,
    @Json(name = "isAvailable")
    val isAvailable: Boolean? = null
)