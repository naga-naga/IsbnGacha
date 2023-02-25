package com.example.isbngacha

class RandomIsbnGenerator : IsbnGenerator {
    override fun generate(): String {
        val number = (0..99999999).random()
        val prefix = "9784"
        val isbnWithoutCheckDigit = prefix + String.format("%08d", number)
        return calculateCheckDigit(isbnWithoutCheckDigit)
    }
}