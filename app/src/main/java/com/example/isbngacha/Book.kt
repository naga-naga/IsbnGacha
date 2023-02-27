package com.example.isbngacha

data class Book(
    val isbn: String,
    var title: String,
    var volume: String,
    var series: String,
    var publisher: String,
    var pubdate: String,
    var cover: String,
    var author: String
) {

    constructor(isbn: String) :
            this(isbn, "", "", "", "", "", "", "")
}