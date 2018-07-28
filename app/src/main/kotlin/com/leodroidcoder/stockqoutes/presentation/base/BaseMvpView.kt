package com.leodroidcoder.stockqoutes.presentation.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseMvpView : MvpView {

        /**
         * An error occurred.
         *
         * @param errorCode code
         * @see [ErrorCodes]
         */
        fun onError(errorCode: Int)
    }