package com.example.project

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.project.APIFiles.BooksItems
import com.example.project.APIFiles.Item
import com.example.project.room.BookDao.BookDao
import com.example.project.room.BookDao.BookDatabase
import com.example.project.room.BookDao.BookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch



private val API_KEY = "AIzaSyDNS4AGFREbQngCxekgCHq_RJoIJfa9tAg";

class BooksViewModel(application: Application) : AndroidViewModel(application) { // les données reçues de l'API
    private val _booksList = mutableStateOf(BooksItems(emptyList<Item>()));
    var booksList = _booksList;
    var repo = BookViewModelRepository();
    var errorMessage: String by mutableStateOf("")
    private val _detail = Item.createDefault()
    var details = _detail
    private val bookDao: BookDao
    private val database: BookDatabase = Room.databaseBuilder(
        application,
        BookDatabase::class.java,
        "favorites_db"
    ).build()

    init {
        bookDao = database.bookDao()
        searchBooks("android programming")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                val response = repo.searchBooks(
                    query,
                    40,
                    "items(id,volumeInfo(title,authors,imageLinks))",
                    0,
                    API_KEY
                )
                booksList.value = response;
            } catch (e: Exception) {
                errorMessage = e.message ?: "An error occurred"
                Log.d("AM", errorMessage);
            }
        }
    }

    fun getFavoriteBooks(): Flow<List<BookEntity>> {
        return bookDao.getAllBooks()
    }

    fun toggleFavorite(book: BookEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (book.isFavorite) {
                bookDao.insertBook(book)
            } else {
                bookDao.deleteBook(book)
            }
        }
    }


    suspend fun detailsBooks(id: String): Item {
        try {
            val reponse = repo.detailsBooks(
                id,
                fields = "id,volumeInfo(title,authors,publisher,publishedDate,description,pageCount,printedPageCount,language,imageLinks/smallThumbnail,imageLinks/thumbnail),saleInfo(country,saleability,isEbook,listPrice,retailPrice,buyLink),accessInfo(country,epub/isAvailable,pdf/isAvailable,webReaderLink)",
                API_KEY
            )
            details = reponse

        } catch (e: Exception) {
            throw e
        }
        return details

    }

}


