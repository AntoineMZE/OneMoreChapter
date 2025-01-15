package com.example.project.room

import android.app.Application
import com.example.project.BookViewModelRepository
import com.example.project.room.BookDao.BookDao
import com.example.project.room.BookDao.BookDatabase

class BooksApplication : Application() {
    val database : BookDatabase by lazy { BookDatabase.getDatabase(this) }
    val dao : BookDao by lazy { database.bookDao() }
    val repository : FavoritesRepository by lazy { FavoritesRepository(dao) }

    val bookRepo : BookViewModelRepository by lazy {  BookViewModelRepository()}
}
