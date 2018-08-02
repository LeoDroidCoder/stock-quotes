package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * WebSockets API.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface WebSocketsApi {

    /**
     * Connect to web sockets.
     *
     * @since 1.0.0
     */
    fun connect(): Completable

    /**
     * Subscribe for connection changes.
     * Emits `true` when connection with sockets client has been established
     * and `false` when it was lost.
     *
     * @since 1.0.0
     */
    fun subscribeForConnectionChanges(): Flowable<Boolean>

    /**
     * Subscribe for new currency pairs.
     * After successful completion [subscribeForTickUpdates] should emit ticks for the pairs [pairs]
     *
     * @param pairs list of currency pairs
     *
     * @since 1.0.0
     */
    fun subscribeForPairs(pairs: List<String>): Completable

    /**
     * Unsubscribe from currency pairs.
     * After successful completion [subscribeForTickUpdates] should not emit [pairs] data anymore.
     *
     * @param pairs list of currency pairs
     *
     * @since 1.0.0
     */
    fun unSubscribeFromPairs(pairs: List<String>): Completable

    /**
     * Subscribe for all [SymbolsSubscription] updates.
     *
     * @since 1.0.0
     */
    fun subscribeForQuoteUpdates(): Flowable<SymbolsSubscription>

    /**
     * Subscribe for all [Tick] updates.
     *
     * @since 1.0.0
     */
    fun subscribeForTickUpdates(): Flowable<List<Tick>>

    /**
     * Disconnect from web sockets.
     *
     * @since 1.0.0
     */
    fun disconnect(): Completable
}