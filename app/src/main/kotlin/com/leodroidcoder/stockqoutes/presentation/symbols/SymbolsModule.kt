package com.leodroidcoder.stockqoutes.presentation.symbols

import com.arellomobile.mvp.MvpPresenter
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsMvpView
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class SymbolsModule {

    @Binds
    abstract fun bindSymbolsPresenter(presenter: SymbolsPresenter): MvpPresenter<SymbolsMvpView>

}