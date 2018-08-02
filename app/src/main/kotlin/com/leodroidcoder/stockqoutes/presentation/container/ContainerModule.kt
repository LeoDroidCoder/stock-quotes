package com.leodroidcoder.stockqoutes.presentation.container

import com.arellomobile.mvp.MvpPresenter
import dagger.Binds
import dagger.Module

/**
 * Container screen Dagfger module.
 * Used to provide screen-related dependencies, such as Presenter
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module
abstract class ContainerModule {

    @Binds
    abstract fun bindContainerPresenter(presenter: ContainerPresenter): MvpPresenter<ContainerMvpView>
}