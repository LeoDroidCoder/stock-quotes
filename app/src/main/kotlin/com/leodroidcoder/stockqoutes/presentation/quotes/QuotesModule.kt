package com.leodroidcoder.stockqoutes.presentation.quotes

import com.arellomobile.mvp.MvpPresenter
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesMvpView
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class QuotesModule {

    @Binds
    abstract fun bindQuotesPresenter(presenter: QuotesPresenter): MvpPresenter<QuotesMvpView>

}