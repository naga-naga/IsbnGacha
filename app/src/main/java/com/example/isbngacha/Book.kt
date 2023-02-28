package com.example.isbngacha

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val isbn: String,
    var title: String?,
    var volume: String?,
    var series: String?,
    var publisher: String?,
    var pubdate: String?,
    @ColumnInfo(name = "cover_url") var coverUrl: String?,
    var author: String?,
    @ColumnInfo(name = "cover_image") var coverImage: Bitmap?,
    @ColumnInfo(name = "last_fetch_datetime") var lastFetchDatetime: String?,
) {

    constructor(isbn: String) :
            this(isbn, "", "", "", "", "", "", "", null, "")
}