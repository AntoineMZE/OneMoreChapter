package com.example.project.APIFiles

import com.squareup.moshi.Json

data class Item(
    @Json(name = "accessInfo")
    val accessInfo: AccessInfo? = null,
    @Json(name = "etag")
    val etag: String? = null,
    @Json(name = "id")
    val id: String,
    @Json(name = "kind")
    val kind: String? = null,
    @Json(name = "saleInfo")
    val saleInfo: SaleInfo? = null,
    @Json(name = "searchInfo")
    val searchInfo: SearchInfo? = null,
    @Json(name = "selfLink")
    val selfLink: String? = null,
    @Json(name = "volumeInfo")
    val volumeInfo: VolumeInfo? = null
) {
    companion object {
        fun createDefault(): Item {
            return Item(
                id = "default-id",
                kind = "default-kind",
                etag = "default-etag",
                selfLink = "https://default.link",
                volumeInfo = null
            )
        }
    }
}