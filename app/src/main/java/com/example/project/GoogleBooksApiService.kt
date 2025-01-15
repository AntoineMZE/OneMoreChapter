package com.example.project

import android.util.Log
import com.example.project.APIFiles.BooksItems
import com.example.project.APIFiles.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") results : Int,
        @Query("fields") fields: String,
        @Query("startIndex") index : Int,
        @Query("key") apiKey: String
    ): BooksItems

    @GET("volumes/{id}")
    suspend fun detailsBooks(
        @Path("id") id: String?,
        @Query("fields") fields: String,
        @Query("key") apiKey: String
    ): Item
}
