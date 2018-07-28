package com.leodroidcoder.stockqoutes.presentation.quotes

import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.presentation.base.BaseMvpView

interface QuotesMvpView : BaseMvpView {

    fun showData(ticks: List<Tick>)
}