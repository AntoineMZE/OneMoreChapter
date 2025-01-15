package com.example.project.APIFiles


import com.squareup.moshi.Json

data class IndustryIdentifier(
    @Json(name = "identifier")
    val identifier: String? = null,
    @Json(name = "type")
    val type: String? = null
)