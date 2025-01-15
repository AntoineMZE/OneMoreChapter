package com.example.project.APIFiles


import com.squareup.moshi.Json

data class VolumeInfo(
    @Json(name = "authors")
    val authors: List<String>? = null,
    @Json(name = "averageRating")
    val averageRating: Double? = null,
    @Json(name = "canonicalVolumeLink")
    val canonicalVolumeLink: String? = null,
    @Json(name = "categories")
    val categories: List<String>? = null,
    @Json(name = "contentVersion")
    val contentVersion: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "imageLinks")
    val imageLinks: ImageLinks? = null,
    @Json(name = "industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>? = null,
    @Json(name = "infoLink")
    val infoLink: String? = null,
    @Json(name = "language")
    val language: String? = null,
    @Json(name = "maturityRating")
    val maturityRating: String? = null,
    @Json(name = "pageCount")
    val pageCount: Int? = null,
    @Json(name = "panelizationSummary")
    val panelizationSummary: PanelizationSummary? = null,
    @Json(name = "previewLink")
    val previewLink: String? = null,
    @Json(name = "printType")
    val printType: String? = null,
    @Json(name = "publishedDate")
    val publishedDate: String? = null,
    @Json(name = "publisher")
    val publisher: String? = null,
    @Json(name = "ratingsCount")
    val ratingsCount: Int? = null,
    @Json(name = "readingModes")
    val readingModes: ReadingModes? = null,
    @Json(name = "subtitle")
    val subtitle: String? = null,
    @Json(name = "title")
    val title: String? = null
)