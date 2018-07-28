package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.data.Constants
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Completable
import io.reactivex.Flowable

interface QuotesRepository {

    /**
     * Connect to web sockets.
     */
    fun connectToSockets(): Completable

    /**
     * Subscribe for [SymbolsSubscription] updates from WebSockets
     */
    fun subscribeForSymbolUpdates(): Flowable<SymbolsSubscription>

    /**
     * Save symbols
     */
    fun saveSymbols(symbolSubs: SymbolsSubscription): Completable

    /**
     * Subscribe for [Tick] updates from WebSockets
     */
    fun subscribeForTickUpdates(): Flowable<List<Tick>>

    /**
     * Save ticks to database.
     */
    fun saveTicks(ticks: List<Tick>): Completable

    /**
     * Disconnect from WebSockets.
     */
    fun disconnectFromSockets(): Completable

    /**
     * Subscribe for currency symbols or unsubscribe from them.
     * Should be used in order to manage for which currency pairs the App should receive tick updates.
     *
     * @param symbols list of currency symbols, some or all of [Constants.ALL_SYMBOLS]
     * @param subscribe Pass in `true` in order to subscribe for pairs.
     * After successful subscription [subscribeForTickUpdates] should emit ticks for the symbols [symbols]
     * Pass in `false` in order to unsubscribe for pairs.
     * After successful completion [subscribeForTickUpdates] should not emit these [symbols] data anymore.
     */
    fun symbolsSubscriptionToggle(symbols: List<String>, subscribe: Boolean): Completable

    /**
     * Get list of symbol subscriptions.
     *
     * @return [Flowable] of all possible symbol pairs [Constants.ALL_SYMBOLS],
     * where [Pair.first] is the symbol itself and [Pair.second] is `true` if we subscribed to the symbol
     * and correspondingly `false` if not.
     */
    fun getSymbolSubscriptions(): Flowable<List<Pair<String, Boolean>>>

    /**
     * Get ticks of a specific symbol from database.
     * Emits fresh data whenever it is changed in db.
     *
     * @param symbol currency pair symbol, for example "EURUSD"
     * @see Constants.ALL_SYMBOLS
     */
    fun getTicks(symbol: String): Flowable<List<Tick>>

    /**
     * Get List of last ticks for every currency pair symbol (to which we subscribed).
     * For instance, if we subscribed for "EURUSD" tick updates, the result will contain
     * a List with a single [Tick] item, which is the last received one.
     */
    fun getLastSymbolTicks(): Flowable<List<Tick>>
}