package com.example.project.APIFiles


import com.squareup.moshi.Json

data class ListPrice(
    @Json(name = "amountInMicros")
    val amountInMicros: Int? = null,
    @Json(name = "currencyCode")
    val currencyCode: String? = null
)