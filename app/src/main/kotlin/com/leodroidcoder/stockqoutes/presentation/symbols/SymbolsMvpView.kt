package com.leodroidcoder.stockqoutes.presentation.symbols

import com.leodroidcoder.stockqoutes.presentation.base.BaseMvpView

/**
 * Symbols screen MVP view contract
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface SymbolsMvpView : BaseMvpView {

    /**
     * Show symbols data.
     *
     * @since 1.0.0
     */
    fun onSymbols(symbols: List<Pair<String, Boolean>>)

}