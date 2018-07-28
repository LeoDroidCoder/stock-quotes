package com.leodroidcoder.stockqoutes.presentation.common.navigation

/**
 * Created by leonid on 9/26/17.
 */
interface BackButtonListener {

    /**
     * Callback, called on back button action in parent activity
     *
     * @since 0.1.0
     * @return true if callback should be passed to next attached fragment
     */
    fun onBackButtonPressed(): Boolean
}