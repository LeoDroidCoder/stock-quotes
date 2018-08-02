package com.leodroidcoder.stockqoutes.di.module

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

/**
 * Navigation module.
 * Provides navigation-related module objects.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module
class NavigationModule {

    // Global cicerone router
    private val globalCiceroneRouter = Cicerone.create()

    /**
     * Provides global router, used to navigate between screens.
     *
     * @return app global router
     *
     * @since 1.0.0
     */
    @Provides
    @Singleton
    fun provideRouter(): Router = globalCiceroneRouter.router

    /**
     * Provides global navigation holder.
     *
     * @return app global navigation holder
     *
     * @since 1.0.0
     */
    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder = globalCiceroneRouter.navigatorHolder

}