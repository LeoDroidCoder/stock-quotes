package com.leodroidcoder.stockqoutes.data.db

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun timestampToDsate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}