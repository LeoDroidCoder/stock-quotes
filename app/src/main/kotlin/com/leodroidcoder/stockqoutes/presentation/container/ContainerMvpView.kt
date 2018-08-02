package com.leodroidcoder.stockqoutes.presentation.container

import com.leodroidcoder.stockqoutes.presentation.base.BaseMvpView

/**
 * Container screen MVP view contract
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface ContainerMvpView : BaseMvpView {

    /**
     * No setting title is needed, this logic is performed in fragments
     *
     * @since 1.0.0
     */
    override fun setupToolbar(title: String, backEnabled: Boolean) {
    }

    /**
     * Connectivity to sockets change.
     * Inform user when it is lost.
     *
     * @param connected `false` if connection is lost and `true` when it is established.
     *
     * @since 1.0.0
     */
    fun onConnectivityChange(connected: Boolean)
}