package com.leodroidcoder.stockqoutes.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.leodroidcoder.stockqoutes.data.db.entity.TickDbEntity

/**
 * Application database
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Database(entities = [(TickDbEntity::class)], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    /**
     * Get Ticks data access object.
     *
     * @since 1.0.0
     */
    abstract fun tickDao(): TickDao

}
