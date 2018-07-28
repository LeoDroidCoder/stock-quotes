package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Completable
import io.reactivex.Flowable

interface WebSocketsApi {

    /**
     * Connect to web sockets.
     */
    fun connect(autoReconnect: Boolean = false): Completable

    /**
     * Subscribe for new currency pairs.
     * After successful completion [subscribeForTickUpdates] should emit ticks for the pairs [pairs]
     *
     * @param pairs list of currency pairs
     */
    fun subscribeForPairs(pairs: List<String>): Completable

    /**
     * Unsubscribe from currency pairs.
     * After successful completion [subscribeForTickUpdates] should not emit [pairs] data anymore.
     *
     * @param pairs list of currency pairs
     */
    fun unSubscribeFromPairs(pairs: List<String>): Completable

    /**
     * Subscribe for all [SymbolsSubscription] updates.
     */
    fun subscribeForQuoteUpdates(): Flowable<SymbolsSubscription>

    /**
     * Subscribe for all [Tick] updates.
     */
    fun subscribeForTickUpdates(): Flowable<List<Tick>>

    /**
     * Disconnect from web sockets.
     */
    fun disconnect(): Completable

}