package com.leodroidcoder.stockqoutes.data

import com.leodroidcoder.stockqoutes.domain.entity.Tick

/**
 * An object, containing all the supported currency pair symbols.
 *
 * @see Tick.symbol
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
object CurrencySymbols {

    /**
     * Returns immutable list of all the supported currency pair symbols.
     *
     * @since 1.0.0
     */
    val ALL_SYMBOLS = listOf(
            "EURUSD",
            "EURGBP",
            "USDJPY",
            "GBPUSD",
            "USDCHF",
            "USDCAD",
            "AUDUSD",
            "EURJPY",
            "EURCHF"
    )
}





