package com.leodroidcoder.stockqoutes.presentation.container

import com.arellomobile.mvp.MvpPresenter
import com.leodroidcoder.stockqoutes.presentation.container.ContainerMvpView
import com.leodroidcoder.stockqoutes.presentation.container.ContainerPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class ContainerModule {

    @Binds
    abstract fun bindContainerPresenter(presenter: ContainerPresenter): MvpPresenter<ContainerMvpView>
}