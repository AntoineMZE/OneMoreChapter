package com.example.project.room.BookDao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class BookEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val authors: String,
    val imageUrl: String,
    var isFavorite: Boolean = false
)
