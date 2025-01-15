package com.example.project.APIFiles


import com.squareup.moshi.Json

data class Offer(
    @Json(name = "finskyOfferType")
    val finskyOfferType: Int,
    @Json(name = "giftable")
    val giftable: Boolean,
    @Json(name = "listPrice")
    val listPrice: ListPrice,
    @Json(name = "retailPrice")
    val retailPrice: RetailPrice
)