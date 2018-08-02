package com.leodroidcoder.stockqoutes.presentation.symbols

import com.arellomobile.mvp.MvpPresenter
import dagger.Binds
import dagger.Module

/**
 * Symbols screen Dagger module.
 * Used to provide screen-related dependencies, such as Presenter
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module
abstract class SymbolsModule {

    @Binds
    abstract fun bindSymbolsPresenter(presenter: SymbolsPresenter): MvpPresenter<SymbolsMvpView>

}