package com.example.project

import com.example.project.APIFiles.BooksItems
import com.example.project.APIFiles.Item

class BookViewModelRepository {

    suspend fun detailsBooks(id: String?, fields : String, apiKey: String ): Item {
        return APIService.booksServices.detailsBooks(id, fields, apiKey)
    }

    suspend fun searchBooks(query: String, maxResult: Int, fields : String, startIndex : Int ,apiKey: String): BooksItems {
        return APIService.booksServices.searchBooks(query, maxResult, fields, startIndex, apiKey )
    }
}