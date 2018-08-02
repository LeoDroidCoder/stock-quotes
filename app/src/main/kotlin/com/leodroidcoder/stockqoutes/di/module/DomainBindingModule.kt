package com.leodroidcoder.stockqoutes.di.module

import android.arch.persistence.room.Dao
import com.leodroidcoder.stockqoutes.data.db.AppDataBase
import com.leodroidcoder.stockqoutes.data.sockets.WebSocketsManager
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.QuotesRepositoryImpl
import com.leodroidcoder.stockqoutes.domain.WebSocketsApi
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Domain binding module.
 * Provides implementation of domain-level APIs, such as [QuotesRepository], [WebSocketsApi].
 * For now the database [AppDataBase] is accessed directly with [Dao] objects in order not to
 * complicate the structure.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module
abstract class DomainBindingModule {

    @Singleton
    @Binds
    abstract fun bindWebSocketsApi(manager: WebSocketsManager): WebSocketsApi

    @Singleton
    @Binds
    abstract fun bindQuotesRepository(repository: QuotesRepositoryImpl): QuotesRepository

}