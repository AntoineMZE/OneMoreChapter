package com.example.project.APIFiles


import com.squareup.moshi.Json

data class BooksItems(
    @Json(name = "items")
    val items: List<Item>,
)