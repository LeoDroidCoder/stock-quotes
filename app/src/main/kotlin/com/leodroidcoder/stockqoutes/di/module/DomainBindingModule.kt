package com.leodroidcoder.stockqoutes.di.module

import com.leodroidcoder.stockqoutes.data.sockets.WebSocketsManager
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.QuotesRepositoryImpl
import com.leodroidcoder.stockqoutes.domain.WebSocketsApi
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainBindingModule {

    @Singleton
    @Binds
    abstract fun bindWebSocketsApi(manager: WebSocketsManager): WebSocketsApi

    @Singleton
    @Binds
    abstract fun bindQuotesRepository(repository: QuotesRepositoryImpl): QuotesRepository

}