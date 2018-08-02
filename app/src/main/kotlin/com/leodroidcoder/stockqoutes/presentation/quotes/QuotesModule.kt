package com.leodroidcoder.stockqoutes.presentation.quotes

import com.arellomobile.mvp.MvpPresenter
import dagger.Binds
import dagger.Module

/**
 * Quotes screen Dagger module.
 * Used to provide screen-related dependencies, such as Presenter
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module
abstract class QuotesModule {

    @Binds
    abstract fun bindQuotesPresenter(presenter: QuotesPresenter): MvpPresenter<QuotesMvpView>

}