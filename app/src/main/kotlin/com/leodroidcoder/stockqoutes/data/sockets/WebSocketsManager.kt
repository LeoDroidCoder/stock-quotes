package com.leodroidcoder.stockqoutes.data.sockets

import com.google.gson.Gson
import com.leodroidcoder.stockqoutes.data.sockets.entity.BaseResponse
import com.leodroidcoder.stockqoutes.data.sockets.entity.PairSubscriptionEntity
import com.leodroidcoder.stockqoutes.data.sockets.entity.TicksSubscriptionsEntity
import com.leodroidcoder.stockqoutes.data.sockets.mapper.QuotesEntityMapper
import com.leodroidcoder.stockqoutes.data.sockets.mapper.TickEntityMapper
import com.leodroidcoder.stockqoutes.domain.WebSocketsApi
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.fromJson
import com.leodroidcoder.stockqoutes.subscribeBy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import timber.log.Timber
import javax.inject.Inject

class WebSocketsManager @Inject constructor(
        private val socketsClient: WebSocketsClient,
        private val gson: Gson
) : WebSocketsApi {

    private val pairsEntityProcessor = PublishProcessor.create<PairSubscriptionEntity>()
    private val tickEntityProcessor = PublishProcessor.create<TicksSubscriptionsEntity>()

    /**
     * Connect to web sockets.
     */
//    override fun connect(autoReconnect: Boolean): Completable {
//        Timber.d("CONNECT")
//        return socketsClient.connect()
//                .andThen() {
//                    socketsClient.subscribeForMessages().doOnNext {
//                        handleMessage(it)
//                        Timber.d("onNext!")
//                    }.subscribe()
//                }
//    }

    override fun connect(autoReconnect: Boolean): Completable {
        Timber.d("CONNECT")
        return socketsClient.connect()
                .doOnComplete {
                    socketsClient.subscribeForMessages()
                            .doOnNext(::handleMessage)
                            .subscribeBy(Timber::e)
                }
    }

    private fun handleMessage(message: String) {
        Timber.d("handleMessage: $message")
        val response = gson.fromJson<BaseResponse>(message)
        when (response) {
            is PairSubscriptionEntity -> pairsEntityProcessor.onNext(response)
            is TicksSubscriptionsEntity -> tickEntityProcessor.onNext(response)
            else -> Timber.e("handleMessage: Failed to deserialize response: $message")
        }
    }

    /**
     * Subscribe for new currency pairs.
     * After successful completion [subscribeForTickUpdates] should emit ticks for the pairs [pairs]
     *
     * @param pairs list of currency pairs
     */
    override fun subscribeForPairs(pairs: List<String>) =
            send("SUBSCRIBE: ${pairs.joinToString(separator = ",")}")

    /**
     * Unsubscribe from currency pairs.
     * After successful completion [subscribeForTickUpdates] should not emit [pairs] data anymore.
     *
     * @param pairs list of currency pairs
     */
    override fun unSubscribeFromPairs(pairs: List<String>): Completable {
        return send("UNSUBSCRIBE: ${pairs.joinToString(separator = ",")}")
    }

    /**
     * Subscribe for all [SymbolsSubscription] updates.
     */
    override fun subscribeForQuoteUpdates(): Flowable<SymbolsSubscription> = pairsEntityProcessor
            .distinctUntilChanged()
            .map { QuotesEntityMapper.mapTo(it) }

    /**
     * Subscribe for all [Tick] updates.
     */
    override fun subscribeForTickUpdates(): Flowable<List<Tick>> {
        return tickEntityProcessor
                .distinctUntilChanged()
                .map { TickEntityMapper.mapTo(it.ticks.orEmpty()) }
    }

    /**
     * Sends ask message to WebSockets.
     *
     */
    private fun send(message: String): Completable {
        Timber.d("Sending message: $message")
        return socketsClient.send(message)
    }

    /**
     * Disconnect from web sockets.
     */
    override fun disconnect() = socketsClient.disconnect()
}
