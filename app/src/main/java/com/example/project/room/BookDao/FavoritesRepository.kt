package com.example.project.room

import com.example.project.APIFiles.BooksItems
import com.example.project.room.BookDao.BookDao
import com.example.project.room.BookDao.BookEntity
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val dao : BookDao) {

    suspend fun insertBook(books: BookEntity){
        dao.insertBook(books);
    }

    suspend fun deleteBooks(books: BookEntity){
        dao.deleteBook(books);
    }


    fun getAllBooks() : Flow<List<BookEntity>> = dao.getAllBooks();
}
