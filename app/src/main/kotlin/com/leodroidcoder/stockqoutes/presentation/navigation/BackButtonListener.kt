package com.leodroidcoder.stockqoutes.presentation.navigation

import ru.terrakok.cicerone.Navigator

/**
 * Back button listener contract.
 * Needed for navigation.
 * Should be implemented by fragments, used with [Navigator]
 *
 * @see Navigator
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface BackButtonListener {

    /**
     * Callback, called on back button action in parent activity
     *
     * @since 0.1.0
     * @return true if callback should be passed to the parent fragment or activity
     */
    fun onBackButtonPressed(): Boolean
}