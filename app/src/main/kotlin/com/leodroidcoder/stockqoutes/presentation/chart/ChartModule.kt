package com.leodroidcoder.stockqoutes.presentation.chart

import com.arellomobile.mvp.MvpPresenter
import com.leodroidcoder.stockqoutes.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ChartModule {

    @Provides
    @FragmentScope
    fun provideChartPresenter(presenter: ChartPresenter): MvpPresenter<ChartMvpView> = presenter

    @Provides
    @FragmentScope
    @Named(SYMBOL_QUALIFIER)
    fun provideSymbol(fragment: ChartFragment): String {
        return fragment.arguments?.getString(ChartFragment.ARGUMENT_SYMBOL) ?: ""
    }

    companion object {
        const val SYMBOL_QUALIFIER = "symbol"
    }
}