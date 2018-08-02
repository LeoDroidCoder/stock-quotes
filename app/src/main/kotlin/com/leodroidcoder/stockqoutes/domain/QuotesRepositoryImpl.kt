package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.data.CurrencySymbols
import com.leodroidcoder.stockqoutes.data.db.AppDataBase
import com.leodroidcoder.stockqoutes.data.db.mapper.TickDbEntityMapper
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository [QuotesRepository] implementation.
 * Represents a `Facade` for receiving/sending any data.
 * Manages multiple data sources, such as [WebSocketsApi] and [AppDataBase],
 * decides whether to load data from remote or local and when to save it.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Singleton
class QuotesRepositoryImpl @Inject constructor(
        private val socketsApi: WebSocketsApi,
        private val dataBase: AppDataBase
) : QuotesRepository {

    /**
     * Connect to web sockets.
     * Subscribes for sockets connectivity changes [WebSocketsApi.subscribeForConnectionChanges].
     * After successful connection subscribes for topics (symbols and ticks)
     * Performs reconnect in [RECONNECT_TIMEOUT] once connection to sockets is lost.
     *
     * @since 1.0.0
     */
    override fun keepSocketsConnection(): Completable =
            socketsApi.subscribeForConnectionChanges()
                    .startWith(false)
                    .flatMapCompletable { it ->
                        if (!it) connectInternal()
                                .retryWhen { t -> t.delay(RECONNECT_TIMEOUT, TimeUnit.MILLISECONDS) }
                        else Completable.complete()
                    }

    /**
     * Connect to web sockets
     * and subscribe for topics (symbols symbols tick updates) [subscribeForCurrentSymbols].
     * Process data received from sockets [saveTicks], [processSymbolSubscriptions]
     *
     * @since 1.0.0
     */
    private fun connectInternal(): Completable = socketsApi.connect()
            .andThen(subscribeForCurrentSymbols())
            .andThen(subscribeForTopics())

    /**
     * Subscribe for all socket topics, such as quotes and ticks.
     * As well as handle (save) received results.
     *
     * @see subscribeForQuotesUpdates
     * @see subscribeForTickUpdates
     *
     * @since 1.0.0
     */
    private fun subscribeForTopics(): Completable =
            subscribeForQuotesUpdates().flatMapCompletable { processSymbolSubscriptions(it) }
                    .mergeWith(subscribeForTickUpdates().flatMapCompletable { saveTicks(it) })

    /**
     * Subscribe for receiving tick updates from sockets.
     * It should be called after connect to sockets in order to let know server
     * for which currency symbols we want to receive updates.
     * After successful completion we should start receiving tick updates for all the stored
     * in database currency symbols.
     *
     * @since 1.0.0
     */
    private fun subscribeForCurrentSymbols(): Completable = dataBase.tickDao()
            .getSubscribedPairs()
            .first(listOf())
            .flatMapCompletable { it ->
                if (!it.isEmpty())
                    socketsApi.subscribeForPairs(it)
                else Completable.complete()
            }

    /**
     * Subscribe for connectivity to sockets changes
     * in order to get notified when we are connected/disconnected.
     * Emit `true` when connection to WebSockets is established and `false` when it is lost.
     *
     * @since 1.0.0
     */
    override fun subscribeForConnectivityChanges():
            Flowable<Boolean> = socketsApi.subscribeForConnectionChanges()

    /**
     * Start listening for symbol subscriptions data from sockets
     * Subscribes for [SymbolsSubscription] updates.
     *
     * @since 1.0.0
     */
    override fun subscribeForQuotesUpdates():
            Flowable<SymbolsSubscription> = socketsApi.subscribeForQuoteUpdates()

    /**
     * Called when symbols subscription has been changed.
     * Clears data of unsubscribed items from db and saves received data on subscribed ones.
     * Handles the following possible cases:
     *  1. Removes from db data of unsubscribed symbols (symbols not included in the response [SymbolsSubscription.ticks]).
     *  2. Saves to db fresh data
     * In case of empty ticks (there are no subscriptions) - clears all ticks from database.
     *
     * @since 1.0.0
     */
    private fun processSymbolSubscriptions(symbolSubs: SymbolsSubscription):
            Completable = retainSymbols(symbolSubs)
            .andThen(saveTicks(symbolSubs.ticks))

    /**
     * Retains the symbols data in database
     * (removes data on all the symbols except present in [SymbolsSubscription.ticks])
     *
     * @since 1.0.0
     */
    private fun retainSymbols(subs: SymbolsSubscription):
            Completable = Observable.fromIterable(subs.ticks)
            .map { it.symbol }
            .toList()
            .flatMapCompletable {
                Completable.fromCallable {
                    dataBase.tickDao().retainQuotes(it)
                }
            }

    /**
     * Get List of last ticks for every currency pair symbol (to which we subscribed).
     * For instance, if we subscribed for "EURUSD" tick updates, the result will contain
     * a List with a single [Tick] item, which is the last received one.
     *
     * @since 1.0.0
     */
    override fun getLastSymbolTicks():
            Flowable<List<Tick>> = dataBase.tickDao().getLastSymbolTicks()
            .distinctUntilChanged()
            .map { TickDbEntityMapper.mapTo(it) }

    /**
     * Subscribe for [Tick] updates from WebSockets
     *
     * @since 1.0.0
     */
    override fun subscribeForTickUpdates():
            Flowable<List<Tick>> = socketsApi.subscribeForTickUpdates()

    /**
     * Saves ticks to database.
     *
     * @since 1.0.0
     */
    private fun saveTicks(ticks: List<Tick>): Completable = Completable.fromAction {
        dataBase.tickDao().insertTicks(TickDbEntityMapper.mapFrom(ticks))
    }

    /**
     * Get ticks of a specific symbol from database.
     * Emits fresh data whenever it is changed in db.
     *
     * @param symbol currency pair symbol, for example "EURUSD"
     * @see CurrencySymbols.ALL_SYMBOLS
     *
     * @since 1.0.0
     */
    override fun getTicks(symbol: String):
            Flowable<List<Tick>> = dataBase.tickDao().getTicks(symbol)
            .distinctUntilChanged()
            .map { TickDbEntityMapper.mapTo(it) }

    /**
     * Get list of symbol subscriptions.
     * Constructs list of pairs, where [Pair.first] - all supported currency symbols
     * and [Pair.second] - whether we subscribed for it's updates or not
     *
     * @return Flowable of all possible symbol pairs [CurrencySymbols.ALL_SYMBOLS],
     * where [Pair.first] is the symbol itself and [Pair.second] is `true` if we subscribed to the symbol
     * and correspondingly `false` if not.
     *
     * @since 1.0.0
     */
    override fun getSymbolSubscriptions():
            Flowable<List<Pair<String, Boolean>>> = dataBase.tickDao().getSubscribedPairs()
            .distinctUntilChanged()
            .flatMapSingle { subscribed ->
                Observable.fromIterable(CurrencySymbols.ALL_SYMBOLS)
                        .map { Pair(it, subscribed.contains(it)) }
                        .toList()
            }

    /**
     * Subscribe for a currency symbol updates.
     *
     * @param symbol a currency symbol, for instance `EURUSD`
     * @see CurrencySymbols.ALL_SYMBOLS
     *
     * @since 1.0.0
     */
    override fun subscribeForSymbol(symbol: String):
            Completable = socketsApi.subscribeForPairs(listOf(symbol))

    /**
     * Unsubscribe from a currency symbol updates.
     *
     * @param symbol a currency symbol, for instance `EURUSD`
     * @see CurrencySymbols.ALL_SYMBOLS
     *
     * @since 1.0.0
     */
    override fun unsubscribeFromSymbol(symbol: String):
            Completable = socketsApi.unSubscribeFromPairs(listOf(symbol))

    /**
     * Disconnect from WebSockets.
     *
     * @see connectInternal
     *
     * @since 1.0.0
     */
    override fun disconnectFromSockets() = socketsApi.disconnect()

    companion object {
        /**
         * Socket reconnection timeout.
         */
        private const val RECONNECT_TIMEOUT = 3000L
    }
}