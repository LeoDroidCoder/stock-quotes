package com.leodroidcoder.stockqoutes.domain.entity

data class SymbolsSubscription(
    val subscribedCount: Int,
    val ticks: List<Tick>
)