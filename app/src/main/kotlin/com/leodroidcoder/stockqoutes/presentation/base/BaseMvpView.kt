package com.leodroidcoder.stockqoutes.presentation.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Base MVP view contact.
 *
 * @see BasePresenter
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseMvpView : MvpView {

    /**
     * Called when toolbar has to be set.
     *
     * @param title toolbar title
     * @param backEnabled whether to show back button or no
     *
     * @since 1.0.0
     */
    fun setupToolbar(title: String = "", backEnabled: Boolean = false)

}