package com.leodroidcoder.stockqoutes.data.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Contains database type converters,
 * used for converting not supported by Room database types into supported and vice-versa.
 * It lets having entity [Entity] properties of any types (almost) to be converted
 * and stored automatically/
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class Converters {

    /**
     * Converts an object of type [Long] into [Date]
     *
     * @since 1.0.0
     */
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    /**
     * Converts an object of type [Date] into [Long]
     *
     * @since 1.0.0
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}