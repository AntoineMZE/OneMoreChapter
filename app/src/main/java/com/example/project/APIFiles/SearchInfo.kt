package com.example.project.APIFiles


import com.squareup.moshi.Json

data class SearchInfo(
    @Json(name = "textSnippet")
    val textSnippet: String? = null
)