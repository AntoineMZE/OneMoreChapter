package com.example.project.APIFiles


import com.squareup.moshi.Json

data class RetailPrice(
    @Json(name = "amount")
    val amount: Double? = null,
    @Json(name = "currencyCode")
    val currencyCode: String? = null
)