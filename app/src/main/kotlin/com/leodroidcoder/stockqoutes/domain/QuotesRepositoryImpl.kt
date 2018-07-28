package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.data.Constants
import com.leodroidcoder.stockqoutes.data.db.AppDataBase
import com.leodroidcoder.stockqoutes.data.db.mapper.TickDbEntityMapper
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesRepositoryImpl @Inject constructor(
        private val socketsApi: WebSocketsApi,
        private val dataBase: AppDataBase
) : QuotesRepository {

    /**
     * Connect to web sockets.
     *
     * @see disconnectFromSockets
     */
    override fun connectToSockets() = socketsApi.connect()

    /**
     * Subscribe for [SymbolsSubscription] updates from WebSockets
     */
    override fun subscribeForSymbolUpdates(): Flowable<SymbolsSubscription> {
        return socketsApi.subscribeForQuoteUpdates()
                .doOnNext { Timber.d("Quotes updated!! size=${it.ticks.size}") }

    }

    /**
     * Save symbols.
     *  1. Removes from db data of unsubscribed symbols (symbols not included in the response [SymbolsSubscription.ticks]).
     *  2. Saves to db fresh data
     */
    override fun saveSymbols(symbolSubs: SymbolsSubscription): Completable {

        Timber.d("saveSymbols")
        return Completable.defer {Completable.fromAction {
            //todo temp
            val tmp = TickDbEntityMapper.mapFrom(symbolSubs.ticks)
            dataBase.tickDao().insertTicks(tmp)
            Timber.d("after save")
//        dataBase.tickDao().insertTicks(TickDbEntityMapper.mapFrom(ticks))
        }
        }


//        Timber.d("saveSymbols before retain :$symbolSubs")
//        return retainSymbols(symbolSubs)
//                .doOnError(Timber::e)
//                .andThen { Timber.d("saveSymbols after Retain}") }
//                .doOnError(Timber::e)
//                .andThen ( saveTicks(symbolSubs.ticks) )
//                .doOnError(Timber::e)
//                .andThen { Timber.e("saveSymbols after save ticks ${symbolSubs.ticks}}") }
//todo stopship!! retain doesn't work
//        return  saveTicks(symbolSubs.ticks)
//                .andThen { Timber.d("saveSymbols after save ticks ${symbolSubs.ticks}}") }

    }

    /**
     * Retains the symbols data in database
     * (removes data on all the symbols except present in [SymbolsSubscription.ticks])
     */
    private fun retainSymbols(subs: SymbolsSubscription): Completable {
        return Observable.fromIterable(subs.ticks)
                .map { it.symbol }
                .toList()
                .flatMapCompletable {
                    Completable.fromCallable {
                        dataBase.tickDao().retainQuotes(it)
                    }
                }
    }

    /**
     * Get List of last ticks for every currency pair symbol (to which we subscribed).
     * For instance, if we subscribed for "EURUSD" tick updates, the result will contain
     * a List with a single [Tick] item, which is the last received one.
     */
    override fun getLastSymbolTicks(): Flowable<List<Tick>> {
        //todo stopship
        return dataBase.tickDao().getAllTicks()
//        return dataBase.tickDao().getLastSymbolTicks()
                .map { TickDbEntityMapper.mapTo(it) }
                .distinctUntilChanged()
    }

    /**
     * Subscribe for [Tick] updates from WebSockets
     */
    override fun subscribeForTickUpdates(): Flowable<List<Tick>> {
        return socketsApi.subscribeForTickUpdates()
    }

    /**
     * Saves ticks to database.
     */
    override fun saveTicks(ticks: List<Tick>): Completable {
        Timber.d("saveTicks !!!!!! ! ! ! ! ! ")
        return Completable.fromAction {
            //todo temp
            val tmp = TickDbEntityMapper.mapFrom(ticks)
            dataBase.tickDao().insertTicks(tmp)
            Timber.d("after save")
//        dataBase.tickDao().insertTicks(TickDbEntityMapper.mapFrom(ticks))
        }
    }

    /**
     * Get ticks of a specific symbol from database.
     * Emits fresh data whenever it is changed in db.
     *
     * @param symbol currency pair symbol, for example "EURUSD"
     * @see Constants.ALL_SYMBOLS
     */
    override fun getTicks(symbol: String): Flowable<List<Tick>> {
        return dataBase.tickDao().getTicks(symbol)
                .distinctUntilChanged()
                .map { TickDbEntityMapper.mapTo(it) }
    }

    /**
     * Get list of symbol subscriptions.
     * Constructs list of pairs, where [Pair.first] - all supported currency symbols
     * and [Pair.second] - whether we subscribed for it's updates or not
     *
     * @return Flowable of all possible symbol pairs [Constants.ALL_SYMBOLS],
     * where [Pair.first] is the symbol itself and [Pair.second] is `true` if we subscribed to the symbol
     * and correspondingly `false` if not.
     */
    override fun getSymbolSubscriptions(): Flowable<List<Pair<String, Boolean>>> {
        return dataBase.tickDao().getSubscribedPairs()
                .distinctUntilChanged()
                .flatMapSingle { subscribed ->
                    Observable.fromIterable(Constants.ALL_SYMBOLS)
                            .map { Pair(it, subscribed.contains(it)) }
                            .toList()
                }
    }

    /**
     * Disconnect from WebSockets.
     *
     * @see connectToSockets
     */
    override fun disconnectFromSockets() = socketsApi.disconnect()

    override fun symbolsSubscriptionToggle(symbols: List<String>, subscribe: Boolean): Completable {
        return if (subscribe)
            socketsApi.subscribeForPairs(symbols)
        else socketsApi.unSubscribeFromPairs(symbols)
    }
}