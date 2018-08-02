package com.leodroidcoder.stockqoutes.domain.entity

/**
 * Domain-layer symbol subscriptions model.
 * Represents subscription/unsubscription to currency symbols result.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
data class SymbolsSubscription(
    val subscribedCount: Int,
    val ticks: List<Tick>
)