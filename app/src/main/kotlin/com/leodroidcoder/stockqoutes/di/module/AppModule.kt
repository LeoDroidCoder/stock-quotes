package com.leodroidcoder.stockqoutes.di.module

import android.content.Context
import com.leodroidcoder.stockqoutes.App
import com.leodroidcoder.stockqoutes.di.qualifier.AppContext
import com.leodroidcoder.stockqoutes.di.scope.ActivityScope
import com.leodroidcoder.stockqoutes.di.scope.FragmentScope
import com.leodroidcoder.stockqoutes.presentation.chart.ChartFragment
import com.leodroidcoder.stockqoutes.presentation.chart.ChartModule
import com.leodroidcoder.stockqoutes.presentation.container.ContainerActivity
import com.leodroidcoder.stockqoutes.presentation.container.ContainerModule
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesFragment
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesModule
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsFragment
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main application module.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module(includes = [(AndroidSupportInjectionModule::class)])
abstract class AppModule {

    @Singleton
    @Binds
    @AppContext
    abstract fun provideContext(app: App): Context

    @ActivityScope
    @ContributesAndroidInjector(modules = [(ContainerModule::class)])
    abstract fun containerActivity(): ContainerActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [(QuotesModule::class)])
    abstract fun quotesFragment(): QuotesFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [(SymbolsModule::class)])
    abstract fun symbolsFragment(): SymbolsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [(ChartModule::class)])
    abstract fun chartFragment(): ChartFragment

}