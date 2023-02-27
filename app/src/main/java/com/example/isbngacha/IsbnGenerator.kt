package com.example.isbngacha

interface IsbnGenerator {
    fun generate(): String

    /**
     * @param isbnWithoutCheckDigit チェックデジットが付いていない ISBN の String
     * @return チェックデジット付きの ISBN の String
     */
    fun addCheckDigit(isbnWithoutCheckDigit: String): String {
        if (isbnWithoutCheckDigit.length != 12) {
            throw java.lang.IllegalArgumentException("isbnWithoutCheckDigit must be 12 characters")
        }

        // --- チェックデジットの求め方 ---
        // 1. 奇数番目の数字の和を求める
        // 2. 偶数番目の数字の和を3倍する
        // 3. 1, 2 で求めた数字を足し，10で割った余りを求める
        // 4. 3 で求めた値に x を足して10になるとき，x がチェックデジットである

        val oddIndexNumbers = mutableListOf<Int>()
        val evenIndexNumbers = mutableListOf<Int>()

        isbnWithoutCheckDigit.toList().forEachIndexed { i, numberChar ->
            val number: Int = numberChar - '0' // Char to Int
            // インデックスが0始まりなので，偶奇が逆になる
            if (i % 2 == 0) {
                oddIndexNumbers.add(number)
            } else {
                evenIndexNumbers.add(number)
            }
        }

        val sumOfOddIndexNumbers = oddIndexNumbers.sum()
        val sumOfEvenIndexNumbers = evenIndexNumbers.sum()
        val checkDigitRemainder = (sumOfOddIndexNumbers + sumOfEvenIndexNumbers * 3) % 10
        val checkDigit = (10 - checkDigitRemainder) % 10

        return isbnWithoutCheckDigit + checkDigit.toString()
    }
}