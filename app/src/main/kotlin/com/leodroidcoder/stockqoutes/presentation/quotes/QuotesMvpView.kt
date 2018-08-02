package com.leodroidcoder.stockqoutes.presentation.quotes

import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.presentation.base.BaseMvpView

/**
 * Quotes screen MVP view contract
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface QuotesMvpView : BaseMvpView {

    /**
     * Show quotes data.
     *
     * @since 1.0.0
     */
    fun showData(ticks: List<Tick>)
}