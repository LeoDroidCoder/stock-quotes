package com.leodroidcoder.stockqoutes.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

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