package com.leodroidcoder.stockqoutes.presentation.symbols

import com.leodroidcoder.stockqoutes.presentation.base.BaseMvpView

interface SymbolsMvpView : BaseMvpView {

    fun onSymbols(symbols: List<Pair<String, Boolean>>)

}