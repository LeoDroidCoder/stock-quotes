package com.leodroidcoder.stockqoutes.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.leodroidcoder.stockqoutes.data.db.entity.TickDbEntity

@Database(entities = arrayOf(TickDbEntity::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    internal abstract fun tickDao(): TickDao

}
