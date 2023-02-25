package com.example.isbngacha

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class IsbnGeneratorTest {
    @Test
    fun 正しくチェックデジットを付けられるか1() {
        val expected = "9784798155302"
        val generator: IsbnGenerator = RandomIsbnGenerator()
        val actual = generator.calculateCheckDigit("978479815530")
        assertEquals(expected, actual)
    }

    @Test
    fun 正しくチェックデジットを付けられるか2() {
        val expected = "9784797398489"
        val generator: IsbnGenerator = RandomIsbnGenerator()
        val actual = generator.calculateCheckDigit("978479739848")
        assertEquals(expected, actual)
    }

    @Test
    fun 正しくチェックデジットを付けられるか3() {
        val expected = "9784297132347"
        val generator: IsbnGenerator = RandomIsbnGenerator()
        val actual = generator.calculateCheckDigit("978429713234")
        assertEquals(expected, actual)
    }

    @Test
    fun 正しくチェックデジットを付けられるか4() {
        val expected = "9784556692490"
        val generator: IsbnGenerator = RandomIsbnGenerator()
        val actual = generator.calculateCheckDigit("978455669249")
        assertEquals(expected, actual)
    }

    @Test
    fun 桁数が正しくないとき1() {
        val generator: IsbnGenerator = RandomIsbnGenerator()
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            generator.calculateCheckDigit("9784")
        }
    }

    @Test
    fun 桁数が正しくないとき2() {
        val generator: IsbnGenerator = RandomIsbnGenerator()
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            generator.calculateCheckDigit("")
        }
    }

    @Test
    fun 桁数が正しくないとき3() {
        val generator: IsbnGenerator = RandomIsbnGenerator()
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            generator.calculateCheckDigit("1234567890123") // 13桁
        }
    }

    @Test
    fun 桁数が正しくないとき4() {
        val generator: IsbnGenerator = RandomIsbnGenerator()
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            generator.calculateCheckDigit("12345678901") // 11桁
        }
    }
}