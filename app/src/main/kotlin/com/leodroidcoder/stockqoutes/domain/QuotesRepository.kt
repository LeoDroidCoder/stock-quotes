package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.data.CurrencySymbols
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Repository API.
 * Any presentation-layer subscriber should use it for accessing any data.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
interface QuotesRepository {

    /**
     * Connect to web sockets.
     * Subscribe for topics after successful connection.
     * Manages reconnects.
     *
     * @since 1.0.0
     */
    fun keepSocketsConnection(): Completable

    /**
     * Subscribe for connectivity to sockets changes
     * in order to get notified when we are connected/disconnected.
     * Emit `true` when connection to WebSockets is established and `false` when it is lost.
     *
     * @since 1.0.0
     */
    fun subscribeForConnectivityChanges(): Flowable<Boolean>

    /**
     * Subscribe for [SymbolsSubscription] "Topic" updates from WebSockets
     *
     * @since 1.0.0
     */
    fun subscribeForQuotesUpdates(): Flowable<SymbolsSubscription>

    /**
     * Subscribe for [Tick] updates "Topic" from WebSockets
     *
     * @since 1.0.0
     */
    fun subscribeForTickUpdates(): Flowable<List<Tick>>

    /**
     * Get list of symbol subscriptions.
     *
     * @return [Flowable] of all possible symbol pairs [CurrencySymbols.ALL_SYMBOLS],
     * where [Pair.first] is the symbol itself and [Pair.second] is `true` if we subscribed to the symbol
     * and correspondingly `false` if not.
     *
     * @since 1.0.0
     */
    fun getSymbolSubscriptions(): Flowable<List<Pair<String, Boolean>>>

    /**
     * Get ticks of a specific symbol from database.
     * Emits fresh data whenever it is changed in db.
     *
     * @param symbol currency pair symbol, for example "EURUSD"
     * @see CurrencySymbols.ALL_SYMBOLS
     *
     * @since 1.0.0
     */
    fun getTicks(symbol: String): Flowable<List<Tick>>

    /**
     * Get List of last ticks for every currency pair symbol (to which we subscribed).
     * For instance, if we subscribed for "EURUSD" tick updates, the result will contain
     * a List with a single [Tick] item, which is the last received one.
     *
     * @since 1.0.0
     */
    fun getLastSymbolTicks(): Flowable<List<Tick>>

    /**
     * Subscribe for a currency symbol updates.
     * Gets currently subscribed symbols from the database,
     * adds [symbol] and sends them all together.
     * After successful subscription [subscribeForTickUpdates] should emit ticks for the symbols [symbol]
     *
     * @param symbol a currency symbol, for instance `EURUSD`
     * @see CurrencySymbols.ALL_SYMBOLS
     *
     * @since 1.0.0
     */
    fun subscribeForSymbol(symbol: String): Completable

    /**
     * Unsubscribe from a currency symbol updates.
     * After successful completion [subscribeForTickUpdates] should not emit these [symbol] data anymore.
     *
     * @param symbol a currency symbol, for instance `EURUSD`
     * @see CurrencySymbols.ALL_SYMBOLS
     *
     * @since 1.0.0
     */
    fun unsubscribeFromSymbol(symbol: String): Completable

    /**
     * Disconnect from WebSockets.
     *
     * @since 1.0.0
     */
    fun disconnectFromSockets(): Completable

}