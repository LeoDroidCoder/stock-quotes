package com.leodroidcoder.stockqoutes.domain.entity

/**
 * Tick [Tick] sorting order enum.
 * Provides comparators for sorting [Tick] by different conditions.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
enum class TickOrder {

    /**
     * Compares [Tick] in ascending order by [Tick.symbol].
     *
     * @since 1.0.0
     */
    SYMBOL_ASC {
        override fun compareTick(): Comparator<Tick> = Comparator { o1, o2 -> o1.symbol.compareTo(o2.symbol) }
    },

    /**
     * Compares [Tick] in descending order by [Tick.symbol].
     *
     * @since 1.0.0
     */
    SYMBOL_DESC {
        override fun compareTick(): Comparator<Tick> = Comparator { o1, o2 -> o2.symbol.compareTo(o1.symbol) }
    };

    abstract fun compareTick(): Comparator<Tick>
}