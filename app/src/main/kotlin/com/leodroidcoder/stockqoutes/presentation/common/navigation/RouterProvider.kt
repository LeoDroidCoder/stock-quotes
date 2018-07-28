package com.leodroidcoder.stockqoutes.presentation.common.navigation

import ru.terrakok.cicerone.Router

/**
 * Created by leonid on 9/26/17.
 */
interface RouterProvider {

    /**
     * Provide all child base navigation fragments with parent router
     *
     * @return router of parent activity
     */
    fun getRouter(): Router
}