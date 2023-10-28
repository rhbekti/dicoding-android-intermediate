package com.dicoding.newsapp.utils

import org.junit.Assert
import org.junit.Test
import java.time.format.DateTimeParseException
import java.time.zone.ZoneRulesException

class DateFormatterTest {
    @Test
    fun `given correct ISO 8601 format then should format correctly`() {
        val currentDate = "2023-10-28T10:10:10Z"
        Assert.assertEquals(
            "28 Oct 2023 | 17:10",
            DateFormatter.formatDate(currentDate, "Asia/Jakarta")
        )
    }

    @Test
    fun `given wrong ISO 8601 format then should throw error`() {
        val wrongFormat = "2023-10-29T10:10"
        Assert.assertThrows(DateTimeParseException::class.java) {
            DateFormatter.formatDate(wrongFormat, "Asia/Jakarta")
        }
    }

    @Test
    fun `given invalid timezone then should throw error`() {
        val wrongFormat = "2023-10-29T10:10:10Z"
        Assert.assertThrows(ZoneRulesException::class.java) {
            DateFormatter.formatDate(wrongFormat, "Asia/Bandung")
        }
    }
}