package com.leodroidcoder.stockqoutes.data.sockets

import com.google.gson.Gson
import com.leodroidcoder.stockqoutes.data.sockets.entity.BaseResponse
import com.leodroidcoder.stockqoutes.data.sockets.entity.PairSubscriptionResponse
import com.leodroidcoder.stockqoutes.data.sockets.entity.TicksResponse
import com.leodroidcoder.stockqoutes.data.sockets.exception.ExceptionFactory
import com.leodroidcoder.stockqoutes.data.sockets.mapper.QuotesEntityMapper
import com.leodroidcoder.stockqoutes.data.sockets.mapper.TickEntityMapper
import com.leodroidcoder.stockqoutes.domain.WebSocketsApi
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.fromJson
import com.neovisionaries.ws.client.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocket manager.
 * Connects to WebSocket client, subscribes for topics,
 * sends and receives messages.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Singleton
class WebSocketsManager @Inject constructor(
        private var webSocket: WebSocket,
        private val gson: Gson
) : WebSocketsApi {

    private val pairsEntityProcessor = PublishProcessor.create<PairSubscriptionResponse>()
    private val tickEntityProcessor = PublishProcessor.create<TicksResponse>()
    private val connectionProcessor = PublishProcessor.create<Boolean>()

    private var listener: WebSocketAdapter? = null

    /**
     * Connect to web sockets.
     * Completes once connected.
     * Emits connection state change to [connectionProcessor],
     * so it can be used to manage reconnects.
     *
     * @since 1.0.0
     */
    override fun connect(): Completable = Completable.create { emitter ->
        listener = object : WebSocketAdapter() {
            /**
             * Called when sockets connection is established
             * Emits connection state change to [connectionProcessor]
             *
             * @since 1.0.0
             */
            override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) {
                super.onConnected(websocket, headers)
                Timber.d("Connected to sockets state =${webSocket.state}")
                connectionProcessor.onNext(true)
                if (!emitter.isDisposed) {
                    emitter.onComplete()
                }
            }

            /**
             * Raw message received.
             * Handles it in [handleMessage]
             *
             * @since 1.0.0
             */
            override fun onTextMessage(websocket: WebSocket, text: String) {
                Timber.d("Received message: $text")
                handleMessage(text)
            }

            /**
             * Called when sockets connection is disconnected.
             * Emits connection state change to [connectionProcessor]
             *
             * @since 1.0.0
             */
            override fun onDisconnected(
                    websocket: WebSocket?, serverCloseFrame: WebSocketFrame?,
                    clientCloseFrame: WebSocketFrame?, closedByServer: Boolean
            ) {
                super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer)
                connectionProcessor.onNext(false)
                Timber.d("Disconnected from sockets")
            }

            /**
             * Called when an error occurred
             * Emits connection state change to [connectionProcessor]
             *
             * @since 1.0.0
             */
            override fun onError(websocket: WebSocket?, error: WebSocketException) {
                super.onError(websocket, error)
                connectionProcessor.onNext(false)
                Timber.d("Sockets error: $error")
                if (!emitter.isDisposed) {
                    emitter.onError(ExceptionFactory.create(error))
                }
            }
        }
        webSocket = webSocket.recreate()
        webSocket.addListener(listener)
        webSocket.connect()
    }
            .doOnError { connectionProcessor.onNext(false) }

    /**
     * Subscribe for connection changes.
     * Emits `true` when connection with sockets client has been established
     * and `false` when it was lost.
     *
     * @since 1.0.0
     */
    override fun subscribeForConnectionChanges():
            Flowable<Boolean> = connectionProcessor.distinctUntilChanged()


    /**
     * A helper function for parsing raw json response int entitoes
     *
     * @see BaseResponse
     *
     * @since 1.0.0
     */
    private fun handleMessage(message: String) {
        val response = gson.fromJson<BaseResponse>(message)
        when (response) {
            is PairSubscriptionResponse -> pairsEntityProcessor.onNext(response)
            is TicksResponse -> tickEntityProcessor.onNext(response)
            else -> Timber.e("handleMessage: Failed to deserialize response: $message")
        }
    }


    /**
     * Subscribe for new currency pairs.
     * After successful completion [subscribeForTickUpdates] should emit ticks for the pairs [pairs]
     *
     * @param pairs list of currency pairs
     *
     * @since 1.0.0
     */
    override fun subscribeForPairs(pairs: List<String>) :
            Completable = send("SUBSCRIBE: ${pairs.joinToString(separator = ",")}")

    /**
     * Unsubscribe from currency pairs.
     * After successful completion [subscribeForTickUpdates] should not emit [pairs] data anymore.
     *
     * @param pairs list of currency pairs
     *
     * @since 1.0.0
     */
    override fun unSubscribeFromPairs(pairs: List<String>):
            Completable = send("UNSUBSCRIBE: ${pairs.joinToString(separator = ",")}")

    /**
     * Helper function for sending a message to websockets
     *
     * @throws IllegalStateException in case of trying to send a message whe sockets are not connected
     *
     * @since 1.0.0
     */
    private fun send(s: String): Completable = Completable.fromAction {
        if (!webSocket.isOpen) {
            throw IllegalStateException("WebSockets are not connected, state=${webSocket.state}. Cannot send command:$s")
        }
        webSocket.sendText(s)
    }

    /**
     * Subscribe for all [SymbolsSubscription] updates.
     *
     * @since 1.0.0
     */
    override fun subscribeForQuoteUpdates():
            Flowable<SymbolsSubscription> = pairsEntityProcessor
            .distinctUntilChanged()
            .map { QuotesEntityMapper.mapTo(it) }

    /**
     * Subscribe for all [Tick] updates.
     *
     * @since 1.0.0
     */
    override fun subscribeForTickUpdates():
            Flowable<List<Tick>> = tickEntityProcessor
            .distinctUntilChanged()
            .map { TickEntityMapper.mapTo(it.ticks.orEmpty()) }

    /**
     * Disconnect from web sockets.
     *
     * @since 1.0.0
     */
    override fun disconnect(): Completable = Completable.fromAction {
        if (webSocket.state != WebSocketState.CLOSED || webSocket.state != WebSocketState.CLOSING) {
            webSocket.removeListener(listener)
            webSocket.disconnect()
        }
    }
}
