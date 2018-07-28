package com.leodroidcoder.stockqoutes.domain.entity

enum class TickOrder {

    SYMBOL_ASC {
        override fun compareTick(): Comparator<Tick> = Comparator { o1, o2 -> o1.symbol.compareTo(o2.symbol) }
    },
    SYMBOL_DESC {
        override fun compareTick(): Comparator<Tick> = Comparator { o1, o2 -> o2.symbol.compareTo(o1.symbol) }
    };

    abstract fun compareTick(): Comparator<Tick>
}