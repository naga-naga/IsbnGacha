package com.example.isbngacha

import androidx.room.*

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): MutableList<Book>

    @Query("SELECT * FROM books WHERE isbn = :isbn")
    suspend fun getBookWithIsbn(isbn: String): Book
}