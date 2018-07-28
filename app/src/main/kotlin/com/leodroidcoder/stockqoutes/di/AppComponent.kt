package com.leodroidcoder.stockqoutes.di

import com.leodroidcoder.stockqoutes.App
import com.leodroidcoder.stockqoutes.di.module.AppModule
import com.leodroidcoder.stockqoutes.di.module.DataModule
import com.leodroidcoder.stockqoutes.di.module.DomainBindingModule
import com.leodroidcoder.stockqoutes.di.module.NavigationModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    DataModule::class,
    NavigationModule::class,
    DomainBindingModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>() {}
}