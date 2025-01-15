package com.example.project.APIFiles


import com.squareup.moshi.Json

data class PanelizationSummary(
    @Json(name = "containsEpubBubbles")
    val containsEpubBubbles: Boolean,
    @Json(name = "containsImageBubbles")
    val containsImageBubbles: Boolean
)