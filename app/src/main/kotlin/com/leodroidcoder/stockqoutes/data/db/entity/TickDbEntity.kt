package com.leodroidcoder.stockqoutes.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.leodroidcoder.stockqoutes.data.db.mapper.TickDbEntityMapper
import java.util.*

/**
 * Tick database entity.
 *
 * @see TickDbEntityMapper
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Entity(tableName = "tick")
data class TickDbEntity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        val symbol: String,
        val bid: Double,
        val bf: Int,
        val ask: Double,
        val af: Int,
        val spread: Double,
        val date: Date = Date()
)