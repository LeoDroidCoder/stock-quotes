package com.leodroidcoder.stockqoutes.di.module

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

/**
 * Created by leonid on 9/26/17.
 */
@Module
class NavigationModule {

    // Global cicerone router
    private val globalCiceroneRouter = Cicerone.create()

    /**
     * Provides app with global router
     *
     * @since 0.1.0
     * @return app global router
     */
    @Provides
    @Singleton
    fun provideRouter(): Router = globalCiceroneRouter.router

    /**
     * Provides app with global navigation holder
     *
     * @since 0.1.0
     * @return app global navigation holder
     */
    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder = globalCiceroneRouter.navigatorHolder

}