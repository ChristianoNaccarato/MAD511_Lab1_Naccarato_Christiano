package com.example.mad511_lab1_naccarato_christiano.data

import androidx.room3.ColumnTypeConverter
import java.time.Instant

class InstantConverter {
    @ColumnTypeConverter
    fun fromInstant(value: Instant): Long = value.toEpochMilli()

    @ColumnTypeConverter
    fun toInstant(value: Long): Instant = Instant.ofEpochMilli(value)
}