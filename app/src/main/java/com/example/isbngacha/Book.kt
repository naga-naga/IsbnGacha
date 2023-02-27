package com.example.isbngacha

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val isbn: String,
    var title: String,
    var volume: String,
    var series: String,
    var publisher: String,
    var pubdate: String,
    var cover: String,
    var author: String
) : Parcelable {

    constructor(isbn: String) :
            this(isbn, "", "", "", "", "", "", "")
}