package com.leodroidcoder.stockqoutes.presentation.chart

import com.leodroidcoder.stockqoutes.presentation.base.BaseMvpView
import com.leodroidcoder.stockqoutes.presentation.chart.chart.ChartModel

/**
 * Chart screen MVP view contract
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface ChartMvpView : BaseMvpView {

    /**
     * Show chart data.
     *
     * @since 1.0.0
     */
    fun showData(chartData: ChartModel)
}