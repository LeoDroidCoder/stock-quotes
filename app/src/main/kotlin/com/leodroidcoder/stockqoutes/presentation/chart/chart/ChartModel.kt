package com.leodroidcoder.stockqoutes.presentation.chart.chart

import com.github.mikephil.charting.data.Entry

data class ChartModel(
        val referenceTime: Long,
        val ask: List<Entry>,
        val bid: List<Entry>
)