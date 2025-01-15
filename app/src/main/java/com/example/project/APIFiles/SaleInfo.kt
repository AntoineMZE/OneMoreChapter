package com.example.project.APIFiles


import com.squareup.moshi.Json

data class SaleInfo(
    @Json(name = "buyLink")
    val buyLink: String? = null,
    @Json(name = "country")
    val country: String? = null,
    @Json(name = "isEbook")
    val isEbook: Boolean? = null,
    @Json(name = "listPrice")
    val listPrice: ListPrice? = null,
    @Json(name = "offers")
    val offers: List<Offer>? = null,
    @Json(name = "retailPrice")
    val retailPrice: RetailPrice? = null,
    @Json(name = "saleability")
    val saleability: String? = null
)