package com.leodroidcoder.stockqoutes.domain.entity

import java.util.*

data class Tick(
        val id: Long? = null,
        val symbol: String,
        val bid: Double,
        val bf: Int,
        val ask: Double,
        val af: Int,
        val spread: Double,
        val date: Date = Date()
) {

    val formattedSymbol: String
        get() = "${symbol.substring(0, 3)} / ${symbol.substring(3, 6)}"
}